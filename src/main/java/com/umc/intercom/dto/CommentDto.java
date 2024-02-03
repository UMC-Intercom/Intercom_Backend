package com.umc.intercom.dto;

import com.umc.intercom.domain.Comment;
import com.umc.intercom.domain.common.enums.AdoptionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    @Getter
    public static class CommentRequestDto {
        private Long talkId;
        private String content;
    }

    @Getter
    public static class ReplyRequestDto {
        private Long talkId;
        private Long parentId;
        private String content;
    }

    @Getter
    @Setter
    @Builder
    public static class CommentResponseDto {
        private Long userId;
        private Long talkId;
        private Long id;
        private Long parentId; // 부모 댓글 ID
        private String content;
        private String writer;
        private AdoptionStatus adoptionStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static CommentResponseDto toDto(Comment comment) {
            return new CommentResponseDto(
                    comment.getUser().getId(),
                    comment.getTalk().getId(),
                    comment.getId(),
                    comment.getParentId() != null ? comment.getParentId().getId() : null, // 부모 댓글 ID
                    comment.getContent(),
                    comment.getUser().getNickname(),
                    comment.getAdoptionStatus(),
                    comment.getCreatedAt(),
                    comment.getCreatedAt()
            );
        }
    }
}