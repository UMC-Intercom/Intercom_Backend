package com.umc.intercom.dto;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.common.enums.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResumeDto {

    @Getter
    public static class ResumeRequestDto {
        @Schema(description = "step1) 지원 회사", example = "회사명")
        private String company;
        @Schema(description = "지원 부서 및 직무", example = "부서 및 직무명")
        private String department;
        @Schema(description = "합격 연도", example = "2024")
        private String year;
        @Schema(description = "상반기 or 하반기", example = "상반기")
        private String semester;

        @Schema(description = "step2) 학력", example = "학교명")
        private String education;
        @Schema(description = "학과", example = "학과명")
        private String major;
        @Schema(description = "학점", example = "4.0/4.5")
        private String gpa;
        @Schema(description = "대외활동", example = "대외활동 내용")
        private String activity;
        @Schema(description = "자격증", example = "자격증1, 자격증2, 자격증3,")
        private List<String> certifications;
        @Schema(description = "어학", example = "어학 종류1, 종류2, 종류3,")
        private String english;
        @Schema(description = "취득 점수", example = "취득 점수1, 점수2, 점수3,")
        private String score;

        @Schema(description = "step3) 자소서 문항", example = "문항")
        private List<String> titles;
        @Schema(description = "자소서 답변", example = "답변")
        private List<String> contents;

    }

    @Getter
    @Setter
    @Builder
    public static class ResumeResponseDto {
        //Post 필드
        private Long id;
        private String company;
        private String department;
        private String year; //합격 년도
        private String semester; //상반기 , 하반기
        private PostType postType;
        private int viewCount;
        private int scrapCount;
        private String writer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        //PostDetail 필드
        private List<String> titles;
        private List<String> contents;
        //PostSpec 필드
        private String education;
        private String major;
        private String gpa;
        private String activity;
        private String certification;
        private String english;
        private String score;

        public static ResumeResponseDto toDto(Post post, List<PostDetail> postDetails, PostSpec postSpec) {
            return new ResumeResponseDto(
                    post.getId(),
                    post.getCompany(),
                    post.getDepartment(),
                    post.getYear(),
                    post.getSemester(),
                    post.getPostType(),
                    post.getViewCount(),
                    post.getScrapCount(),
                    post.getUser().getNickname(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    postDetails.stream().map(PostDetail::getTitle).collect(Collectors.toList()),
                    postDetails.stream().map(PostDetail::getContent).collect(Collectors.toList()),
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
        private int scrapCount;
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
                .scrapCount(post.getScrapCount())
                .writer(post.getUser().getNickname())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .build();
    }

}
