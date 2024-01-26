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

}
