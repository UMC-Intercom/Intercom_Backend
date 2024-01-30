package com.umc.intercom.controller;

import java.util.List;

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
    
    @PostMapping
    public ResponseEntity<InterviewDto> createInterview(@RequestBody InterviewDto interviewDto) {
        String userEmail = SecurityUtil.getCurrentUsername();
        InterviewDto createdInterviewDto = interviewService.createInterview(interviewDto, userEmail);
        return new ResponseEntity<>(createdInterviewDto, HttpStatus.CREATED);
    }
    
    // 최신순으로 정렬된 검색 결과를 보여줌
    @GetMapping
    public ResponseEntity<List<InterviewDto>> getAllInterviews() {
        List<InterviewDto> interviewList = interviewService.getAllInterviews();
        return new ResponseEntity<>(interviewList, HttpStatus.OK);
    }
}
