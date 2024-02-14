package com.umc.intercom.service;

import com.umc.intercom.domain.Company;
import com.umc.intercom.repository.CompanyRepository;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    private final NaverApiClient naverApiClient;
    private final CompanyRepository companyRepository;

    public CompanyService(NaverApiClient naverApiClient, CompanyRepository companyRepository) {
        this.naverApiClient = naverApiClient;
        this.companyRepository = companyRepository;
    }

    public Company getCompanyLogo(String name) {
        Company company = naverApiClient.getCompanyLogo(name);
        return company;
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }
}
