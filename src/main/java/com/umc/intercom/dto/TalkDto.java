package com.umc.intercom.dto;

import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.common.enums.Category;
import lombok.*;
import org.springframework.data.domain.Page;

import static com.umc.intercom.dto.TalkDto.TalkResponseDto.toDto;

@Data
@NoArgsConstructor
public class TalkDto {
    @Getter
    public static class TalkRequestDto {
        String title;
        String content;
        String category;
        String imageUrl;
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
        private String writer;

        public static TalkResponseDto toDto(Talk talk) {
            return TalkResponseDto.builder()
                    .id(talk.getId())
                    .title(talk.getTitle())
                    .content(talk.getContent())
                    .category(talk.getCategory())
                    .imageUrl(talk.getImageUrl())
                    .viewCount(talk.getViewCount())
                    .writer(talk.getUser().getNickname())
                    .build();
        }
    }

    public static Page<TalkResponseDto> toDtoPage(Page<Talk> talkPage) {
        return talkPage.map(talk -> toDto(talk));
    }
}