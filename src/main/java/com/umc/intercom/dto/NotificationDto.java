package com.umc.intercom.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class NotificationDto {
    @Getter
    @Builder
    public static class NotificationResponseDto {
        private Long id;
        private String writer;  // 댓글 작성자의 닉네임
        private Long commentId;
        private String comment;    // 댓글 내용
        private Long talkId;
        private String talkTitle;
        private boolean isRead;
    }
}
