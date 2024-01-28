package com.umc.intercom.dto;

import com.umc.intercom.domain.Comment;
import com.umc.intercom.domain.common.enums.AdoptionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long userId;
    private Long talkId;
    private Long id;
    private String content;
    private String writer;
    private AdoptionStatus adoptionStatus;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(
                comment.getUser().getId(),
                comment.getTalk().getId(),
                comment.getId(),
                comment.getContent(),
                comment.getUser().getNickname(),
                comment.getAdoptionStatus()
        );
    }
}