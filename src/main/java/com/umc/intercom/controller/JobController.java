package com.umc.intercom.controller;

import com.umc.intercom.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @Operation(summary = "사람인 api 호출 후 DB에 응답 저장", description = "채용정보 API는 1일 최대 500회 호출 가능합니다.")
    @PostMapping("/save")
    public ResponseEntity<Void> saveJob() {
        jobService.saveJobs();
        return ResponseEntity.ok().build();
    }

}
