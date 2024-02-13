package com.umc.intercom.service;

import com.umc.intercom.domain.Company;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NaverApiClient {

    private final RestTemplate restTemplate;
    private final String clientId;
    private final String clientSecret;

    public NaverApiClient(@Value("${naver.client.id}") String clientId,
                          @Value("${naver.client.secret}") String clientSecret,
                          RestTemplateBuilder restTemplateBuilder) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate = restTemplateBuilder.build();
    }

    public Company getCompanyLogo(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://openapi.naver.com/v1/search/image?query=" + name; // API endpoint
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        Company company = new Company();

        return company;
    }
}
