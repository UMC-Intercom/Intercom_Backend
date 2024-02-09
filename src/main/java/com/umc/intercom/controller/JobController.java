package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.JobDto;
import com.umc.intercom.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "관심분야에 해당하는 공고 목록 조회", description = "관심분야 예시: IT개발·데이터, 마케팅·홍보·조사, 디자인")
    @GetMapping("/by-category")
    public ResponseEntity<Page<JobDto.JobListResponseDto>> getJobsByCategory(@RequestParam String interest, @RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();   // 이게 토큰으로 로그인된 사용자인지 검증하는 거라서 비회원일 땐 필요x
        Page<JobDto.JobListResponseDto> jobPages = jobService.getJobsByCategory(userEmail, interest, page);
        return ResponseEntity.ok(jobPages);
    }
}
