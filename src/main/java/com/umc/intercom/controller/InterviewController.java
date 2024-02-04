package com.umc.intercom.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.InterviewDto;
import com.umc.intercom.service.InterviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {
    
    private final InterviewService interviewService;

    @Operation(summary = "면접 후기 게시글 작성")
    @PostMapping
    public ResponseEntity<InterviewDto.InterviewResponseDto> createInterview(@RequestBody InterviewDto.InterviewRequestDto interviewRequestDto) {
        String userEmail = SecurityUtil.getCurrentUsername();
        InterviewDto.InterviewResponseDto  createdInterviewDto = interviewService.createInterview(interviewRequestDto, userEmail);
        return new ResponseEntity<>(createdInterviewDto, HttpStatus.CREATED);
    }

    @Operation(summary = "면접 후기 게시글 목록 조회", description = "서버 응답으로 페이징 구현 필요")
    // 최신순으로 정렬된 검색 결과를 보여줌
    @GetMapping
    public ResponseEntity<List<InterviewDto.InterviewResponseDto>> getAllInterviews() {
        List<InterviewDto.InterviewResponseDto> interviewList = interviewService.getAllInterviews();
        return new ResponseEntity<>(interviewList, HttpStatus.OK);
    }
}
