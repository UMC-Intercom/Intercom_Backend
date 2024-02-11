package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.*;
import com.umc.intercom.service.LikeScrapService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LikeScrapController {

    private final LikeScrapService likeScrapService;

    // 좋아요
    @Operation(summary = "톡톡 게시글에 좋아요 추가", description = "{talkId} 자리에 좋아요할 톡톡 게시글 id를 전달해주세요.")
    @PostMapping("/likes/talks/{talkId}")
    public ResponseEntity<LikeScrapDto.LikeScrapResponseDto> addLike(@PathVariable Long talkId) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto.LikeScrapResponseDto likeScrapDto = likeScrapService.addLike(talkId, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    @Operation(summary = "톡톡 게시글에 좋아요 삭제", description = "{talkId} 자리에 좋아요 삭제할 톡톡 게시글 id를 전달해주세요.")
    @DeleteMapping("/likes/talks/{talkId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long talkId) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deleteLike(talkId, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "톡톡 좋아요 여부 확인", description = "{id} 자리에 톡톡 id를 전달해주세요. 좋아요한 경우 true, 좋아요 안 한 경우 false 반환")
    @GetMapping("/likes/talks/{id}")
    public ResponseEntity<Boolean> validateIfUserLikedTalk(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Boolean isLiked = likeScrapService.validateIfUserLikedTalk(userEmail, id);
        return ResponseEntity.ok(isLiked);
    }

    @Operation(summary = "댓글에 좋아요 추가", description = "{id} 자리에 좋아요할 댓글(답글) id를 전달해주세요.")
    @PostMapping("/likes/comments/{id}")
    public ResponseEntity<LikeScrapDto.CommentLikeScrapResponseDto> addLikeComment(@PathVariable Long id) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto.CommentLikeScrapResponseDto likeScrapDto = likeScrapService.addCommentLike(id, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    @Operation(summary = "댓글에 좋아요 삭제", description = "{talkId} 자리에 좋아요 삭제할 톡톡 게시글 id를 전달해주세요.")
    @DeleteMapping("/likes/comments/{id}")
    public ResponseEntity<Void> deleteLikeComment(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deleteLikeComment(id, userEmail);
        return ResponseEntity.ok().build();
    }
    
    // 스크랩
    @Operation(summary = "톡톡 게시글에 스크랩 추가", description = "{talkId} 자리에 스크랩할 톡톡 게시글 id를 전달해주세요.")
    @PostMapping("/scraps/talks/{talkId}")
    public ResponseEntity<LikeScrapDto.LikeScrapResponseDto> addTalkScrap(@PathVariable Long talkId) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto.LikeScrapResponseDto likeScrapDto = likeScrapService.addTalkScrap(talkId, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    @Operation(summary = "톡톡 게시글에 스크랩 삭제", description = "{talkId} 자리에 스크랩 삭제할 톡톡 게시글 id를 전달해주세요.")
    @DeleteMapping("/scraps/talks/{talkId}")
    public ResponseEntity<Void> deleteTalkScrap(@PathVariable Long talkId) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deleteTalkScrap(talkId, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "면접 후기 or 합격 자소서 게시글에 스크랩 추가", description = "{postId} 자리에 스크랩할 게시글 id를 전달해주세요.")
    @PostMapping("/scraps/posts/{postId}")
    public ResponseEntity<LikeScrapDto.LikeScrapResponseDto> addPostScrap(@PathVariable Long postId) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto.LikeScrapResponseDto likeScrapDto = likeScrapService.addPostScrap(postId, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    @Operation(summary = "면접 후기 or 합격 자소서 게시글에 스크랩 삭제", description = "{postId} 자리에 스크랩 삭제할 게시글 id를 전달해주세요.")
    @DeleteMapping("/scraps/posts/{postId}")
    public ResponseEntity<Void> deletePostScrap(@PathVariable Long postId) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deletePostScrap(postId, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "톡톡 게시글 스크랩 내역 조회")
    @GetMapping("/scraps/talks")
    public ResponseEntity<Page<TalkDto.TalkResponseDto>> getAllTalkScraps(@RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<TalkDto.TalkResponseDto> talkDtoPage = likeScrapService.getAllTalkScraps(userEmail, page);
        return ResponseEntity.ok(talkDtoPage);
    }

    @Operation(summary = "톡톡 스크랩 여부 확인", description = "{id} 자리에 톡톡 id를 전달해주세요. 스크랩한 경우 true, 스크랩 안 한 경우 false 반환")
    @GetMapping("/scraps/talks/{id}")
    public ResponseEntity<Boolean> validateIfUserScrapedTalk(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Boolean isScraped = likeScrapService.validateIfUserScrapedTalk(userEmail, id);
        return ResponseEntity.ok(isScraped);
    }

    @GetMapping("/scraps/interviews")
    @Operation(summary = "면접 후기 게시글 스크랩 내역 조회")
    public ResponseEntity<Page<InterviewDto.InterviewResponseDto>> getAllInterviewScraps(@RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<InterviewDto.InterviewResponseDto> interviewDto = likeScrapService.getAllInterviewScraps(userEmail, page);
        return ResponseEntity.ok(interviewDto);
    }

    @Operation(summary = "합격 자소서 스크랩 내역 조회")
    @GetMapping("/scraps/resumes")
    public ResponseEntity<Page<ResumeDto.ResumeResponseDto>> getAllResumeScraps(@RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<ResumeDto.ResumeResponseDto> resumeDto = likeScrapService.getAllResumeScraps(userEmail, page);
        return ResponseEntity.ok(resumeDto);
    }

    @Operation(summary = "면접 후기 or 합격 자소서 스크랩 여부 확인", description = "{id} 자리에 게시글 id를 전달해주세요. 스크랩한 경우 true, 스크랩 안 한 경우 false 반환")
    @GetMapping("/scraps/posts/{id}")
    public ResponseEntity<Boolean> validateIfUserScrapedPost(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Boolean isScraped = likeScrapService.validateIfUserScrapedPost(userEmail, id);
        return ResponseEntity.ok(isScraped);
    }

    @Operation(summary = "공고 스크랩 추가", description = "{id} 자리에 스크랩할 공고 id를 전달해주세요.")
    @PostMapping("/scraps/jobs/{id}")
    public ResponseEntity<LikeScrapDto.LikeScrapResponseDto> addJobScrap(@PathVariable Long id) throws Exception {
        String userEmail = SecurityUtil.getCurrentUsername();
        LikeScrapDto.LikeScrapResponseDto likeScrapDto = likeScrapService.addJobScrap(id, userEmail);
        return ResponseEntity.ok(likeScrapDto);
    }

    @Operation(summary = "공고 스크랩 삭제", description = "{id} 자리에 스크랩 삭제할 공고 id를 전달해주세요.")
    @DeleteMapping("/scraps/jobs/{id}")
    public ResponseEntity<Void> deleteJobScrap(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        likeScrapService.deleteJobScrap(id, userEmail);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공고 스크랩 내역 조회")
    @GetMapping("/scraps/jobs")
    public ResponseEntity<Page<JobDto.JobListResponseDto>> getAllJobScraps(@RequestParam(value = "page", defaultValue = "1") int page) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Page<JobDto.JobListResponseDto> jobDto = likeScrapService.getAllJobScraps(userEmail, page);
        return ResponseEntity.ok(jobDto);
    }

    @Operation(summary = "공고 스크랩 여부 확인", description = "{id} 자리에 공고 id를 전달해주세요. 스크랩한 경우 true, 스크랩 안 한 경우 false 반환")
    @GetMapping("/scraps/jobs/{id}")
    public ResponseEntity<Boolean> validateIfUserScrapedJob(@PathVariable Long id) {
        String userEmail = SecurityUtil.getCurrentUsername();
        Boolean isScraped = likeScrapService.validateIfUserScrapedJob(userEmail, id);
        return ResponseEntity.ok(isScraped);
    }

}
