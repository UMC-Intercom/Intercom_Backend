package com.umc.intercom.service;

import com.umc.intercom.domain.Company;
import com.umc.intercom.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService {

    private final NaverApiClient naverApiClient;
    private final CompanyRepository companyRepository;

    public CompanyService(NaverApiClient naverApiClient, CompanyRepository companyRepository) {
        this.naverApiClient = naverApiClient;
        this.companyRepository = companyRepository;
    }

    public Company getCompanyLogo(String name) {
        Optional<Company> existingCompany = companyRepository.findByName(name);
        if (existingCompany.isPresent()) {
            return existingCompany.get();
        }

        Company company = naverApiClient.getCompanyLogo(name);
        return companyRepository.save(company);
    }

    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }
}
