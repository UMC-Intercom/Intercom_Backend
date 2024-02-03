package com.umc.intercom.dto;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.common.enums.PostType;

import lombok.*;

import java.time.LocalDateTime;

@Data
public class InterviewDto {

    @Getter
    public static class InterviewRequestDto {
        private String company;
        private String department;
        private String year;
        private String semester;

        private String title;
        private String content;
        private String imageUrl;

        private String education;
        private String major;
        private String gpa;
        private String activity;
        private String certification;
        private String english;
        private String score;
    }

    @Getter
    @Setter
    @Builder
    public static class InterviewResponseDto {
        // Post
        private Long id;
        private String company;
        private String department;
        private String year; //합격 년도
        private String semester; //상반기 , 하반기
        private PostType postType;
        private int viewCount;
        private String writer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // PostDetail
        private String title;
        private String content;
        private String imageUrl;

        // PostSpec
        private String education;
        private String major;
        private String gpa;
        private String activity;
        private String certification;
        private String english;
        private String score;

        public static InterviewResponseDto toDto(Post post, PostDetail postDetail, PostSpec postSpec) {
            return new InterviewResponseDto(
                    post.getId(),
                    post.getCompany(),
                    post.getDepartment(),
                    post.getYear(),
                    post.getSemester(),
                    post.getPostType(),
                    post.getViewCount(),
                    post.getUser().getNickname(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    postDetail.getTitle(),
                    postDetail.getContent(),
                    postDetail.getImageUrl(),
                    postSpec.getEducation(),
                    postSpec.getMajor(),
                    postSpec.getGpa(),
                    postSpec.getActivity(),
                    postSpec.getCertification(),
                    postSpec.getEnglish(),
                    postSpec.getScore()
            );
        }
    }

    @Getter
    @Builder
    public static class ScrapResponseDto {
        private Long id;
        private String company;
        private String department;
        private String year;
        private String semester;
        private PostType postType;
        private int viewCount;
        private String writer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    public static ScrapResponseDto toScrapListDto(Post post) {
        return ScrapResponseDto.builder()
                .id(post.getId())
                .company(post.getCompany())
                .department(post.getDepartment())
                .year(post.getYear())
                .semester(post.getSemester())
                .postType(post.getPostType())
                .viewCount(post.getViewCount())
                .writer(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
