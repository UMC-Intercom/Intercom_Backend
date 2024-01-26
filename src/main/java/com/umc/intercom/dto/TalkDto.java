package com.umc.intercom.dto;

import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.common.enums.Category;
import lombok.*;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TalkDto {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private String imageUrl;
    private int viewCount;
    private String writer;

    public static TalkDto toDto(Talk talk) {
        return new TalkDto(
                talk.getId(),
                talk.getTitle(),
                talk.getContent(),
                talk.getCategory(),
                talk.getImageUrl(),
                talk.getViewCount(),
                talk.getUser().getNickname()
        );
    }

    public static Page<TalkDto> toDtoPage(Page<Talk> talkPage) {
        return talkPage.map(TalkDto::toDto);
    }
}