package com.umc.intercom.controller;

import com.umc.intercom.dto.InterviewDto;
import com.umc.intercom.dto.ResumeDto;
import com.umc.intercom.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    SearchService searchService;


    @Operation(summary = "직무로 자소서 가져오기")
    @GetMapping("/resumes")
    public Page<ResumeDto.ResumeResponseDto> searchResumesByDepartment(@RequestParam String department, @RequestParam(value = "page", defaultValue = "1") int page) {
        return searchService.searchResumesByDepartment(department, page);
    }

    @Operation(summary = "직무로 면접후기 가져오기")
    @GetMapping("/interviews")
    public Page<InterviewDto.InterviewResponseDto> searchInterviewsByDepartment(@RequestParam String department, @RequestParam(value = "page", defaultValue = "1") int page) {
        return searchService.searchInterviewsByDepartment(department, page);
    }
}


