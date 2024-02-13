package com.umc.intercom.controller;

import com.umc.intercom.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<Object>> searchResumesByDepartment(@RequestParam String department){
        List<Object> posts = searchService.searchResumesByDepartment(department);
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "직무로 인터뷰 가져오기")
    @GetMapping("interviews")
    public ResponseEntity<List<Object>> searchInterviewsByDepartment(@RequestParam String department){
        List<Object> posts = searchService.searchInterviewsByDepartment(department);
        return ResponseEntity.ok(posts);
    }
}
