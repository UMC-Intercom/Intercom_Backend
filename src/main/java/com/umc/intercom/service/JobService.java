package com.umc.intercom.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class JobService {

    //    private final JobRepository jobRepository;

    @Value("${api.accessKey}")
    private String ACCESS_KEY;

    public void saveJobs() {
        try {
            String text = URLEncoder.encode("4 11", "UTF-8");   // 공백으로 복수검색 가능(4: 인턴직, 11: 인턴직 (정규직 전환가능))
            String apiURL = "https://oapi.saramin.co.kr/job-search?access-key=" + ACCESS_KEY + "&job_type=" + text;

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            System.out.println("** " + response);

            br.close();

            // response를 parse해서 DB에 저장



        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
