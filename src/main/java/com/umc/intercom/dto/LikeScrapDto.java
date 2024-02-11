package com.umc.intercom.dto;

import com.umc.intercom.domain.LikeScrap;
import com.umc.intercom.domain.common.enums.LikeScrapType;
import com.umc.intercom.domain.common.enums.PostType;
import lombok.*;

@Data
public class LikeScrapDto {

    @Builder
    @Getter
    public static class LikeScrapResponseDto {
        private Long id;
        private LikeScrapType likeScrapType;
        private String nickname;
        private Long targetId;  // Talk 또는 Post 또는 Job id
        private PostType postType;

        // Talk 좋아요 및 스크랩
        public static LikeScrapResponseDto toDtoFromTalk(LikeScrap likeScrap) {
            return LikeScrapResponseDto.builder()
                    .id(likeScrap.getId())
                    .likeScrapType(likeScrap.getLikeScrapType())
                    .nickname(likeScrap.getUser().getNickname())
                    .targetId(likeScrap.getTalk().getId())
                    .postType(likeScrap.getPostType())
                    .build();
        }

        // Post 스크랩
        public static LikeScrapResponseDto toDtoFromPost(LikeScrap likeScrap) {
            return LikeScrapResponseDto.builder()
                    .id(likeScrap.getId())
                    .likeScrapType(likeScrap.getLikeScrapType())
                    .nickname(likeScrap.getUser().getNickname())
                    .targetId(likeScrap.getPost().getId())
                    .postType(likeScrap.getPostType())
                    .build();
        }

        public static LikeScrapResponseDto toDtoFromJob(LikeScrap likeScrap) {
            return LikeScrapResponseDto.builder()
                    .id(likeScrap.getId())
                    .likeScrapType(likeScrap.getLikeScrapType())
                    .nickname(likeScrap.getUser().getNickname())
                    .targetId(likeScrap.getJob().getId())
                    .postType(likeScrap.getPostType())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CommentLikeScrapResponseDto {
        private Long id;
        private LikeScrapType likeScrapType;
        private String nickname;
        private Long targetId;  // Talk 또는 Post 또는 Job id
        private PostType postType;
        private int likeCount;

        public static CommentLikeScrapResponseDto toDtoFromComment(LikeScrap likeScrap) {
            return new CommentLikeScrapResponseDto(
                    likeScrap.getId(),
                    likeScrap.getLikeScrapType(),
                    likeScrap.getUser().getNickname(),
                    likeScrap.getComment().getId(),
                    likeScrap.getPostType(),
                    likeScrap.getComment().getLikeCount()
            );
        }
    }
}