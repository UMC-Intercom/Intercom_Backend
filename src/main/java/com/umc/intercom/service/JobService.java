package com.umc.intercom.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.intercom.domain.Job;
import com.umc.intercom.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Value("${api.accessKey}")
    private String ACCESS_KEY;

    public void saveJobs() {
        int dataPerRequest = 110;   // 한번에 최대 110개 data 조회 가능
        int page = 0;

        while (true) {
            StringBuilder response = new StringBuilder();

            try {
                String text = URLEncoder.encode("4 11", "UTF-8");   // 공백으로 복수검색 가능(4: 인턴직, 11: 인턴직 (정규직 전환가능))
                String apiURL = "https://oapi.saramin.co.kr/job-search?access-key=" + ACCESS_KEY + "&job_type=" + text + "&start=" + page + "&count=" + dataPerRequest;

                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept", "application/json");

                int responseCode = con.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                String inputLine;
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

//            System.out.println("** " + response);

                br.close();

            } catch (Exception e) {
                System.out.println(e);
            }

            // response를 parse해서 DB에 저장
            String jsonResponse = response.toString();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                JsonNode jobArrayNode = rootNode.path("jobs").path("job");

                // API 응답에서 데이터가 없을 경우 종료
                if (!jobArrayNode.iterator().hasNext()) {
                    break;
                }

                for (JsonNode jobNode : jobArrayNode) {
                    // "active" 값이 1인 공고만 가져오기(진행중인 공고)
                    int activeStatus = jobNode.path("active").asInt();

                    if (activeStatus == 1) {
                        // Json 데이터를 엔티티로 변환
                        Job job = convertJsonToJob(jobNode);

                        // DB에 존재하지 않는 공고만 저장
                        if (!jobRepository.existsByJobId(job.getJobId())) {
                            jobRepository.save(job);
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
            page++;
        }
    }

    private Job convertJsonToJob(JsonNode jobNode) {
        
        // 트리 구조로 특정 값에 접근 가능
        return Job.builder()
                .jobId(jobNode.path("id").asText())
                .url(jobNode.path("url").asText())
                .company(jobNode.path("company").path("name").asText())
                .title(jobNode.path("position").path("title").asText())
                .industry(jobNode.path("position").path("industry").path("name").asText())
                .location(jobNode.path("position").path("location").path("name").asText())
                .jobMidCode(jobNode.path("position").path("job-mid-code").path("name").asText())
                .jobCode(jobNode.path("position").path("job-code").path("name").asText())
                .experienceLevel(jobNode.path("position").path("experience-level").path("name").asText())
                .educationLevel(jobNode.path("position").path("required-education-level").path("name").asText())
                .keyword(jobNode.path("keyword").asText())
                .salary(jobNode.path("salary").path("name").asText())
                .postingDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(jobNode.path("posting-timestamp").asLong()), ZoneId.systemDefault()).toLocalDate())
                .modificationDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(jobNode.path("modification-timestamp").asLong()), ZoneId.systemDefault()).toLocalDate())
                .openingDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(jobNode.path("opening-timestamp").asLong()), ZoneId.systemDefault()).toLocalDate())
                .expirationDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(jobNode.path("expiration-timestamp").asLong()), ZoneId.systemDefault()).toLocalDate())
                .closeType(jobNode.path("close-type").path("name").asText())
                .build();
    }
}
