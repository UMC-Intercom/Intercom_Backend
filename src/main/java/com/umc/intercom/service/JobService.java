package com.umc.intercom.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.domain.Company;
import com.umc.intercom.domain.Job;
import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.JobDto;
import com.umc.intercom.repository.JobRepository;
import com.umc.intercom.repository.LikeScrapRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final LikeScrapRepository likeScrapRepository;
    private final UserRepository userRepository;
    private final CompanyService companyService;

    public void saveCompanyLogos() {
        List<Job> jobs = jobRepository.findAll();

        for (Job job : jobs) {
            String companyName = job.getCompany();
            Company company = companyService.getCompanyLogo(companyName);
            if (company != null) {
                companyService.saveCompany(company);
            }
        }
    }

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
                .company(jobNode.path("company").path("detail").path("name").asText())
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

    public Page<JobDto.JobListResponseDto> getJobsByCategory(String userEmail, String interest, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("viewCount"));
        sorts.add(Sort.Order.desc("postingDate"));  // 조회수가 같으면 최근 게시된 순으로

        Pageable pageable = PageRequest.of(page-1, 24, Sort.by(sorts));     // 페이지 당 데이터 24개씩 가져옴

        Page<Job> jobPage = jobRepository.findAllByJobMidCodeContaining(interest, pageable);
        // 현재 회원이 스크랩한 공고 목록 가져오기
        List<Long> userScrapedJobIds = likeScrapRepository.findJobIdsByUserEmailAndPostType(userEmail, PostType.JOB_INFO);

        // 스크랩 여부를 포함하여 DTO로 변환
        return JobDto.JobListResponseDto.toDtoPageWithScrap(jobPage, userScrapedJobIds);
    }

    public Page<JobDto.JobListResponseDto> getJobsByCount(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("viewCount"));
        sorts.add(Sort.Order.desc("postingDate")); // 조회수가 같으면 최신 게시물 우선으로 정렬

        Pageable pageable = PageRequest.of(page - 1, 24, Sort.by(sorts));

        Page<Job> jobPage = jobRepository.findAll(pageable);
        return JobDto.JobListResponseDto.toDtoPageWithScrap(jobPage, Collections.emptyList());
    }

    public Optional<JobDto.JobDetailsResponseDto> getJobById(Long id) {
        Optional<Job> optionalJob = jobRepository.findById(id);

        return optionalJob.map(job -> {
            String userEmail = SecurityUtil.getCurrentUsername();
            return JobDto.JobDetailsResponseDto.builder()
                    .id(job.getId())
                    .jobId(job.getJobId())
                    .url(job.getUrl())
                    .company(job.getCompany())
                    .title(job.getTitle())
                    .industry(job.getIndustry())
                    .location(job.getLocation())
                    .jobMidCode(job.getJobMidCode())
                    .jobCode(job.getJobCode())
                    .experienceLevel(job.getExperienceLevel())
                    .educationLevel(job.getEducationLevel())
                    .keyword(job.getKeyword())
                    .salary(job.getSalary())
                    .postingDate(job.getPostingDate())
                    .modificationDate(job.getModificationDate())
                    .openingDate(job.getOpeningDate())
                    .expirationDate(job.getExpirationDate())
                    .closeType(job.getCloseType())
                    .viewCount(job.getViewCount())
                    .build();
        });
    }

    public Page<JobDto.JobListResponseDto> searchJob(String userEmail, String jobMidCode, String location, String keyword, int page)  {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("postingDate"));
        Pageable pageable = PageRequest.of(page - 1, 24, Sort.by(sorts));

        Page<Job> jobPage;

        if (location.equals("all")) {
            if (keyword == null) {
                jobPage = jobRepository.findByJobMidCodeContaining(jobMidCode, pageable);
            } else {
                jobPage = jobRepository.findByJobMidCodeContainingAndTitleContaining(jobMidCode, keyword, pageable);
            }
        } else {
            if (keyword == null) {
                jobPage = jobRepository.findByJobMidCodeContainingAndLocationContaining(jobMidCode, location, pageable);
            } else {
                jobPage = jobRepository.findByJobMidCodeContainingAndLocationContainingAndTitleContaining(jobMidCode, location, keyword, pageable);
            }
        }

        List<Long> userScrapedJobIds = likeScrapRepository.findJobIdsByUserEmailAndPostType(userEmail, PostType.JOB_INFO);

        // 스크랩 여부를 포함하여 DTO로 변환
        return JobDto.JobListResponseDto.toDtoPageWithScrap(jobPage, userScrapedJobIds);
    }

}
