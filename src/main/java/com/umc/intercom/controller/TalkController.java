package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.TalkDto;
import com.umc.intercom.service.TalkService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "톡톡 게시글 작성")
    @PostMapping
    public ResponseEntity<TalkDto.TalkResponseDto> createTalk(@RequestBody TalkDto.TalkRequestDto talkRequestDto) {
        // 현재 로그인한 유저의 이메일
        String userEmail = SecurityUtil.getCurrentUsername();

        TalkDto.TalkResponseDto createdTalkDto = talkService.createTalk(talkRequestDto, userEmail);
        return new ResponseEntity<>(createdTalkDto, HttpStatus.CREATED);
    }

    // talk 최신 순 리스트 조회(페이징 처리)
    @Operation(summary = "최신 순으로 톡톡 게시글 목록 조회")
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
    @Operation(summary = "조회수 많은 순으로 톡톡 게시글 목록 조회")
    @GetMapping("/view-counts")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalksByViewCounts(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkDtoPage = talkService.getAllTalksByViewCounts(page);
        return ResponseEntity.ok(talkDtoPage);
    }

    // talk 조회수 순 리스트 조회(페이징 처리)
    @Operation(summary = "좋아요 많은 순으로 톡톡 게시글 목록 조회")
    @GetMapping("/like-counts")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalksByLikeCounts(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkDtoPage = talkService.getAllTalksByLikeCounts(page);
        return ResponseEntity.ok(talkDtoPage);
    }

    // talk 상세 조회
    @Operation(summary = "톡톡 게시글 상세 조회", description = "{id} 자리에 상세 조회할 톡톡 게시글 id를 전달해주세요.")
    @GetMapping("/{id}")
    public ResponseEntity<TalkDto.TalkResponseDto> getTalkById(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Optional<TalkDto.TalkResponseDto> optionalTalkDto = talkService.getTalkById(userEmail, id);

        return optionalTalkDto.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 제목으로 talk 검색
    @Operation(summary = "톡톡 게시글 검색", description = "제목으로 검색")
    @GetMapping("/search")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> searchTalksByTitle(@RequestParam("title") String title,
                                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<TalkDto.TalkResponseDto> talkPageDto = talkService.searchTalksByTitle(title, page);
        return ResponseEntity.ok(talkPageDto);
    }

}
