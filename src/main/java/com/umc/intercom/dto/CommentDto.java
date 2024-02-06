package com.umc.intercom.dto;

import com.umc.intercom.domain.Comment;
import com.umc.intercom.domain.common.enums.AdoptionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class CommentDto {

    @Getter
    public static class CommentRequestDto {
        @Schema(description = "댓글 달 톡톡 게시글 id")
        private Long talkId;
        @Schema(description = "댓글 내용", example = "댓글 내용")
        private String content;
    }

    @Getter
    public static class ReplyRequestDto {
        @Schema(description = "댓글 달 톡톡 게시글 id")
        private Long talkId;
        @Schema(description = "대댓글 달 부모 댓글의 id")
        private Long parentId;
        @Schema(description = "댓글 내용", example = "댓글 내용")
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