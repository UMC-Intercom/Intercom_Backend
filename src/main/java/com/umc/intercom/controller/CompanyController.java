package com.umc.intercom.controller;

import com.umc.intercom.domain.Company;
import com.umc.intercom.service.CompanyService;
import com.umc.intercom.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final JobService jobService;

    public CompanyController(CompanyService companyService, JobService jobService) {
        this.companyService = companyService;
        this.jobService = jobService;
    }

    @Operation(summary = "네이버 검색 api 호출 후 DB에 응답 저장")
    @PostMapping("/save")
    public ResponseEntity<Void> saveCompanyLogos() {
        jobService.saveCompanyLogos();
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "기업 로고 검색")
    @GetMapping("/logo")
    public ResponseEntity<Company> getCompanyLogo(@RequestParam String name) {
        return ResponseEntity.ok(companyService.getCompanyLogo(name));
    }
}