package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.TalkDto;
import com.umc.intercom.service.TalkService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/talks")
public class TalkController {

    private final TalkService talkService;

    // talk 생성
    @PostMapping
    public ResponseEntity<TalkDto.TalkResponseDto> createTalk(@RequestBody TalkDto.TalkRequestDto talkRequestDto) {
        // 현재 로그인한 유저의 이메일
        String userEmail = SecurityUtil.getCurrentUsername();

        TalkDto.TalkResponseDto createdTalkDto = talkService.createTalk(talkRequestDto, userEmail);
        return new ResponseEntity<>(createdTalkDto, HttpStatus.CREATED);
    }

    // talk 최신 순 리스트 조회(페이징 처리)
    @GetMapping
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalks(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkDtoPage = talkService.getAllTalks(page);

        // 스웨거 테스트 시 확인
//        System.out.println("현재 페이지 데이터 수(범위 넘어가면 0): " + talkDtoPage.getContent().size());
//        System.out.println("현재 페이지 번호: " + (talkDtoPage.getNumber() + 1));
//        System.out.println("총 페이지 수: " + talkDtoPage.getTotalPages());
//        System.out.println("총 데이터 수: " + talkDtoPage.getTotalElements());

        return ResponseEntity.ok(talkDtoPage);
    }

    // talk 조회수 순 리스트 조회(페이징 처리)
    @GetMapping("/view-counts")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalksByViewCounts(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkDtoPage = talkService.getAllTalksByViewCounts(page);
        return ResponseEntity.ok(talkDtoPage);
    }

    // talk 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<TalkDto.TalkResponseDto> getTalkById(@PathVariable Long id) {
        Optional<TalkDto.TalkResponseDto> optionalTalkDto = talkService.getTalkById(id);

        return optionalTalkDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 제목으로 talk 검색
    @GetMapping("/search")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> searchTalksByTitle(@RequestParam("title") String title,
                                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkPageDto = talkService.searchTalksByTitle(title, page);
        return ResponseEntity.ok(talkPageDto);
    }

}
