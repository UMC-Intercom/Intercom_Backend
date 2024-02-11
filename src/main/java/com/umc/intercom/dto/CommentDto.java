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
        private Long talkId;
        private Long id;
        private Long parentId; // 부모 댓글 ID
        private String content;
        private String writer;
        private AdoptionStatus adoptionStatus;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private int replyCount;
        private String defaultProfile;  // 댓글 작성자의 프로필

        public static CommentResponseDto toDto(Comment comment) {
            return CommentResponseDto.builder()
                    .talkId(comment.getTalk().getId())
                    .id(comment.getId())
                    .parentId(comment.getParentId() != null ? comment.getParentId().getId() : null)
                    .content(comment.getContent())
                    .writer(comment.getUser().getNickname())
                    .adoptionStatus(comment.getAdoptionStatus())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .defaultProfile(comment.getUser().getDefaultProfile())
                    .build();
        }

        public static CommentResponseDto toDto(Comment comment, int replyCount) {
            return CommentResponseDto.builder()
                    .talkId(comment.getTalk().getId())
                    .id(comment.getId())
                    .parentId(comment.getParentId() != null ? comment.getParentId().getId() : null)
                    .content(comment.getContent())
                    .writer(comment.getUser().getNickname())
                    .adoptionStatus(comment.getAdoptionStatus())
                    .createdAt(comment.getCreatedAt())
                    .updatedAt(comment.getUpdatedAt())
                    .replyCount(replyCount)
                    .defaultProfile(comment.getUser().getDefaultProfile())
                    .build();
        }
    }
}