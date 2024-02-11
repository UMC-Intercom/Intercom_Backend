package com.umc.intercom.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.InterviewDto;
import com.umc.intercom.service.InterviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/interviews")
@RequiredArgsConstructor
public class InterviewController {
    
    private final InterviewService interviewService;

    @Operation(summary = "면접 후기 게시글 작성", description = "interviewRequestDto 데이터 형식: \n\n" +
            "{ \"company\": \"회사명\", \"department\": \"부서 및 직무명\", \"year\": \"2024\", \"semester\": \"상반기\", " +
            "\"education\": \"학교명\", \"major\": \"학과명\", \"gpa\": \"4.0/4.5\", \"activity\": \"대외활동 내용\", \"certification\": \"자격증1, 자격증2, 자격증3,\", \"english\": \"어학 종류1, 종류2, 종류3,\", \"score\": \"취득 점수1, 점수2, 점수3,\", " +
            "\"title\": \"문항\", \"content\": \"답변\" }")
    @PostMapping(value = "", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<InterviewDto.InterviewResponseDto> createInterview(@RequestPart(value = "file", required = false) List<MultipartFile> files,
                                                                             @RequestPart("interviewRequestDto") String interviewRequestDtoString) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        InterviewDto.InterviewRequestDto interviewRequestDto = objectMapper.readValue(interviewRequestDtoString, InterviewDto.InterviewRequestDto.class);

        String userEmail = SecurityUtil.getCurrentUsername();
        InterviewDto.InterviewResponseDto  createdInterviewDto = interviewService.createInterview(files, interviewRequestDto, userEmail);
        return new ResponseEntity<>(createdInterviewDto, HttpStatus.CREATED);
    }

    @Operation(summary = "면접 후기 게시글 목록 조회", description = "서버 응답으로 페이징 구현 필요")
    // 최신순으로 정렬된 검색 결과를 보여줌
    @GetMapping
    public ResponseEntity<List<InterviewDto.InterviewResponseDto>> getAllInterviews(@RequestParam(value = "page", defaultValue = "1") int page) {
        List<InterviewDto.InterviewResponseDto> interviewList = interviewService.getAllInterviews(page);
        return new ResponseEntity<>(interviewList, HttpStatus.OK);
    }

    @Operation(summary = "스크랩 수 순으로 면접 후기 게시글 목록 조회")
    @GetMapping("/scrap-counts")
    public ResponseEntity<Page<InterviewDto.InterviewResponseDto>> getAllInterviewsByScrapCounts(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<InterviewDto.InterviewResponseDto> interviewDtoPage = interviewService.getAllInterviewsByScrapCounts(page);
        return ResponseEntity.ok(interviewDtoPage);
    }
}
