package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.ResumeDto;
import com.umc.intercom.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

    @GetMapping
    public ResponseEntity<Page<ResumeDto>> getAllResumes(@RequestParam(value = "page", defaultValue = "1") int page){
        Page<ResumeDto> resumeDtoPage = resumeService.getAllResumes(page);

        // 스웨거 테스트 시 확인
//        System.out.println("현재 페이지 데이터 수(범위 넘어가면 0): " + resumeDtoPage.getContent().size());
//        System.out.println("현재 페이지 번호: " + (resumeDtoPage.getNumber() + 1));
//        System.out.println("총 페이지 수: " + resumeDtoPage.getTotalPages());
//        System.out.println("총 데이터 수: " + resumeDtoPage.getTotalElements());

        return ResponseEntity.ok(resumeDtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeDto> getResumeById(@PathVariable Long id){
        Optional<ResumeDto> optionalResumeDto = resumeService.getResumeById(id);

        if(optionalResumeDto.isPresent()){
            return ResponseEntity.ok(optionalResumeDto.get());
        } else{
            return ResponseEntity.notFound().build();
        }
    }
}
