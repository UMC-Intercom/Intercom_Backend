package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.LikeScrapDto;
import com.umc.intercom.service.LikeScrapService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
