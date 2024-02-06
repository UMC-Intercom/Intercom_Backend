package com.umc.intercom.dto;

import com.umc.intercom.domain.Talk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import static com.umc.intercom.dto.TalkDto.TalkResponseDto.toDto;

@Data
@NoArgsConstructor
@Builder
public class TalkDto {
    @Getter
    @Builder
    public static class TalkRequestDto {
        @Schema(description = "제목", example = "제목")
        private String title;
        @Schema(description = "내용", example = "내용")
        private String content;
        @Schema(description = "카테고리", example = "경영/사무, 마케팅/홍보,")
        private String category;
    }

    @Getter
    @Setter
    @Builder
    public static class TalkResponseDto {
        private Long id;
        private String title;
        private String content;
        private String category;
        private String imageUrl;
        private int viewCount;
        private int likeCount;
        private int commentCount;
        private String writer;

        public static TalkResponseDto toDto(Talk talk) {
            return TalkResponseDto.builder()
                    .id(talk.getId())
                    .title(talk.getTitle())
                    .content(talk.getContent())
                    .category(talk.getCategory())
                    .imageUrl(talk.getImageUrl())
                    .viewCount(talk.getViewCount())
                    .likeCount(talk.getLikeCount())
                    .commentCount(talk.getCommentCount())
                    .writer(talk.getUser().getNickname())
                    .build();
        }
    }

    public static Page<TalkResponseDto> toDtoPage(Page<Talk> talkPage) {
        return talkPage.map(talk -> toDto(talk));
    }
}