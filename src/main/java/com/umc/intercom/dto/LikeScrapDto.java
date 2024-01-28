package com.umc.intercom.dto;

import com.umc.intercom.domain.LikeScrap;
import com.umc.intercom.domain.common.enums.LikeScrapType;
import com.umc.intercom.domain.common.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeScrapDto {
    private Long id;
    private LikeScrapType likeScrapType;
    private String nickname;
    private Long targetId;  // Talk 또는 Post id
    private PostType postType;

    // Talk 좋아요 및 스크랩
    public static LikeScrapDto toDtoFromTalk(LikeScrap likeScrap) {
        return new LikeScrapDto(
                likeScrap.getId(),
                likeScrap.getLikeScrapType(),
                likeScrap.getUser().getNickname(),
                likeScrap.getTalk().getId(),
                likeScrap.getPostType()
        );
    }

    // Post 스크랩
    public static LikeScrapDto toDtoFromPost(LikeScrap likeScrap) {
        return new LikeScrapDto(
                likeScrap.getId(),
                likeScrap.getLikeScrapType(),
                likeScrap.getUser().getNickname(),
                likeScrap.getPost().getId(),
                likeScrap.getPostType()
        );
    }
}