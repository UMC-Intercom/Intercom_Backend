package com.umc.intercom.dto;

import com.umc.intercom.domain.Talk;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static com.umc.intercom.dto.TalkDto.TalkResponseDto.toDto;

@Data
@NoArgsConstructor
@Builder
public class TalkDto {
    @Getter
    @Builder
    public static class TalkRequestDto {
        @Schema(description = "게시글 id", example = "필수x. 임시저장된 글을 저장하는 경우에만 필요")
        private Long id;
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
        private List<String> imageUrls;
        private int viewCount;
        private int likeCount;
        private int scrapCount;
        private int commentCount;
        private int replyCount;
        private String writer;
        private String mentorField;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public static TalkResponseDto toDto(Talk talk) {
            return TalkResponseDto.builder()
                    .id(talk.getId())
                    .title(talk.getTitle())
                    .content(talk.getContent())
                    .category(talk.getCategory())
                    .imageUrls(talk.getImageUrls())
                    .viewCount(talk.getViewCount())
                    .likeCount(talk.getLikeCount())
                    .scrapCount(talk.getScrapCount())
                    .commentCount(talk.getCommentCount())
                    .replyCount(talk.getReplyCount())
                    .writer(talk.getUser().getNickname())
                    .mentorField(talk.getUser().getMentorField())
                    .createdAt(talk.getCreatedAt())
                    .updatedAt(talk.getUpdatedAt())
                    .build();
        }
    }

    public static Page<TalkResponseDto> toDtoPage(Page<Talk> talkPage) {
        return talkPage.map(talk -> toDto(talk));
    }
}