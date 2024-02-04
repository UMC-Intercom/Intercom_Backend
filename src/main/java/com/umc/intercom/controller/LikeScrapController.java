package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.InterviewDto;
import com.umc.intercom.dto.LikeScrapDto;
import com.umc.intercom.dto.ResumeDto;
import com.umc.intercom.dto.TalkDto;
import com.umc.intercom.service.LikeScrapService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LikeScrapController {

    private final LikeScrapService likeScrapService;

    // talk 좋아요 추가
    @PostMapping("/likes/{talkId}")
    public ResponseEntity<LikeScrapDto> addLike(@PathVariable Long talkId) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto likeScrapDto = likeScrapService.addLike(talkId, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    // talk 좋아요 삭제
    @DeleteMapping("/likes/{talkId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long talkId) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deleteLike(talkId, userEmail);
        return ResponseEntity.ok().build();
    }


    // talk 스크랩 추가
    @PostMapping("/scraps/talks/{talkId}")
    public ResponseEntity<LikeScrapDto> addTalkScrap(@PathVariable Long talkId) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto likeScrapDto = likeScrapService.addTalkScrap(talkId, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    // talk 스크랩 삭제
    @DeleteMapping("/scraps/talks/{talkId}")
    public ResponseEntity<Void> deleteTalkScrap(@PathVariable Long talkId) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deleteTalkScrap(talkId, userEmail);
        return ResponseEntity.ok().build();
    }

    // post 스크랩 추가
    @PostMapping("/scraps/posts/{postId}")
    public ResponseEntity<LikeScrapDto> addPostScrap(@PathVariable Long postId) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto likeScrapDto = likeScrapService.addPostScrap(postId, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    // post 스크랩 삭제
    @DeleteMapping("/scraps/posts/{postId}")
    public ResponseEntity<Void> deletePostScrap(@PathVariable Long postId) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deletePostScrap(postId, userEmail);
        return ResponseEntity.ok().build();
    }

    // talk 스크랩 내역 조회
    @GetMapping("/scraps/talks")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalkScraps(@RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<TalkDto.TalkResponseDto> talkDtoPage = likeScrapService.getAllTalkScraps(userEmail, page);

        return ResponseEntity.ok(talkDtoPage);
    }

    // 면접 후기 스크랩 내역 조회
    @GetMapping("/scraps/interviews")
    public ResponseEntity<Page<InterviewDto.ScrapResponseDto>> getAllInterviewScraps(@RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<InterviewDto.ScrapResponseDto> interviewDto = likeScrapService.getAllInterviewScraps(userEmail, page);

        return ResponseEntity.ok(interviewDto);
    }

    // 합격 자소서 스크랩 내역 조회
    @GetMapping("/scraps/resumes")
    public ResponseEntity<Page<ResumeDto.ScrapResponseDto>> getAllResumeScraps(@RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<ResumeDto.ScrapResponseDto> resumeDto = likeScrapService.getAllResumeScraps(userEmail, page);

        return ResponseEntity.ok(resumeDto);
    }
}
