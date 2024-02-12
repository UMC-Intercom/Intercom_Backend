package com.umc.intercom.dto;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.common.enums.PostType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InterviewDto {

    @Getter
    public static class InterviewRequestDto {
        @Schema(description = "step1) 지원 회사", example = "회사명")
        private String company;
        @Schema(description = "지원 부서 및 직무", example = "부서 및 직무명")
        private String department;
        @Schema(description = "합격 연도", example = "2024")
        private String year;
        @Schema(description = "상반기 or 하반기", example = "상반기")
        private String semester;
        @Schema(description = "성별", example = "male/female/no-selected")
        private String gender;
        @Schema(description = "생일", example = "2024-02-12")
        private LocalDate birthday;

        @Schema(description = "step2) 학력", example = "학교명")
        private String education;
        @Schema(description = "학과", example = "학과명")
        private String major;
        @Schema(description = "학점", example = "4.0/4.5")
        private String gpa;
        @Schema(description = "대외활동", example = "대외활동 내용")
        private String activity;
        @Schema(description = "자격증", example = "자격증1, 자격증2, 자격증3,")
        private String certification;
        @Schema(description = "어학", example = "어학 종류1, 종류2, 종류3,")
        private String english;
        @Schema(description = "취득 점수", example = "취득 점수1, 점수2, 점수3,")
        private String score;

        @Schema(description = "step3) 면접 후기 제목", example = "제목")
        private String title;
        @Schema(description = "면접 후기 내용", example = "내용")
        private String content;
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
        private String gender;
        private LocalDate birthday;
        private PostType postType;
        private int viewCount;
        private int scrapCount;
        private String writer;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        // PostDetail
        private String title;
        private String content;
        private List<String> imageUrls;

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
                    post.getGender().toString(),
                    post.getBirthday(),
                    post.getPostType(),
                    post.getViewCount(),
                    post.getScrapCount(),
                    post.getUser().getNickname(),
                    post.getCreatedAt(),
                    post.getUpdatedAt(),
                    postDetail.getTitle(),
                    postDetail.getContent(),
                    postDetail.getImageUrls(),
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
}
