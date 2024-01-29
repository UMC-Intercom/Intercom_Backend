package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.ResumeDto;
import com.umc.intercom.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping
    public ResponseEntity<ResumeDto> createResume(@RequestBody ResumeDto resumeDto){
        String userEmail = SecurityUtil.getCurrentUsername();

        ResumeDto createdResumeDto = resumeService.createResume(resumeDto, userEmail);
        return new ResponseEntity<>(createdResumeDto, HttpStatus.CREATED);
    }
}
