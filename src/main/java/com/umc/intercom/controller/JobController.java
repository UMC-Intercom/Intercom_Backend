package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.JobDto;
import com.umc.intercom.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @Operation(summary = "사람인 api 호출 후 DB에 응답 저장 - 프론트에서 사용x. 서버 api", description = "채용정보 API는 1일 최대 500회 호출 가능합니다.")
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

    @Operation(summary = "조회수 순으로 공고 목록 조회(비로그인)", description = "조회수 내림차순, 조회수 동일 시 최신 순")
    @GetMapping("/by-count")
    public ResponseEntity<Page<JobDto.JobListResponseDto>> getJobsByCount(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<JobDto.JobListResponseDto> jobPages = jobService.getJobsByCount(page);
        return ResponseEntity.ok(jobPages);
    }

    @Operation(summary = "공고 게시글 상세 조회", description = "{id} 자리에 상세 조회할 공고 게시글 id를 전달해주세요.")
    @GetMapping("/{id}")
    public ResponseEntity<JobDto.JobDetailsResponseDto> getJobById(@PathVariable Long id) {
        Optional<JobDto.JobDetailsResponseDto> optionalJobDto = jobService.getJobById(id);

        return optionalJobDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "공고 검색", description = "jobMidCode 예시: IT개발·데이터, 총무·법무·사무, 인사·노무·HRD" +
                "지역으로 '지역 제한 없음'을 선택하는 경우 location 값으로 'all'을 전달해주세요.")
    @GetMapping("/search")
    public ResponseEntity<Page<JobDto.JobListResponseDto>> searchJob(@RequestParam(value = "jobMidCode") String jobMidCode,
                                                                     @RequestParam(value = "location") String location,
                                                                     @RequestParam(value = "keyword", required = false) String keyword,
                                                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<JobDto.JobListResponseDto> jobPages = jobService.searchJob(userEmail, jobMidCode, location, keyword, page);
        return ResponseEntity.ok(jobPages);
    }

}
