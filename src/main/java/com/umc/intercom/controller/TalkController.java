package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.domain.Talk;
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
    public ResponseEntity<Talk> createTalk(@RequestBody Talk talk) {
        // 현재 로그인한 유저의 이메일
        String userEmail = SecurityUtil.getCurrentUsername();

        Talk createdTalk = talkService.createTalk(talk, userEmail);
        return new ResponseEntity<>(createdTalk, HttpStatus.CREATED);
    }

    // talk 최신 순 리스트 조회(페이징 처리)
    @GetMapping
    public ResponseEntity<Page<Talk>> getAllTalks(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Talk> talkPage = talkService.getAllTalks(page);

        // 스웨거 테스트 시 확인
//        System.out.println("현재 페이지 데이터 수(범위 넘어가면 0): " + talkPage.getContent().size());
//        System.out.println("현재 페이지 번호: " + (talkPage.getNumber() + 1));
//        System.out.println("총 페이지 수: " + talkPage.getTotalPages());
//        System.out.println("총 데이터 수: " + talkPage.getTotalElements());

        return ResponseEntity.ok(talkPage);
    }

    // talk 조회수 순 리스트 조회(페이징 처리)
    @GetMapping("/view-counts")
    public ResponseEntity<Page<Talk>> getAllTalksByViewCounts(@RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Talk> talkPage = talkService.getAllTalksByViewCounts(page);
        return ResponseEntity.ok(talkPage);
    }

    // talk 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<Talk> getTalkById(@PathVariable Long id) {
        Optional<Talk> optionalTalk = talkService.getTalkById(id);

        if (optionalTalk.isPresent()) {
            return ResponseEntity.ok(optionalTalk.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 제목으로 talk 검색
    @GetMapping("/search")
    public ResponseEntity<Page<Talk>> searchTalksByTitle(@RequestParam("title") String title,
                                                         @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Talk> talkPage = talkService.searchTalksByTitle(title, page);
        return ResponseEntity.ok(talkPage);
    }

}
