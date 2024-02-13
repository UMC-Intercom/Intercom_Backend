package com.umc.intercom.service;

import com.umc.intercom.domain.Company;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final NaverApiClient naverApiClient;

    public CompanyService(NaverApiClient naverApiClient) {
        this.naverApiClient = naverApiClient;
    }

    public Company getCompanyLogo(String name) {
        return naverApiClient.getCompanyLogo(name);
    }
}
