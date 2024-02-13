package com.umc.intercom.controller;

import com.umc.intercom.domain.Company;
import com.umc.intercom.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/logo")
    public ResponseEntity<Company> getCompanyLogo(@RequestParam String name) {
        return ResponseEntity.ok(companyService.getCompanyLogo(name));
    }
}