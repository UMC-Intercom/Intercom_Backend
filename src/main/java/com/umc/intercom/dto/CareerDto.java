package com.umc.intercom.dto;

import com.umc.intercom.domain.Activity;
import com.umc.intercom.domain.Career;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CareerDto {
    @Getter
    public static class CareerRequestDto {
        //Career 필드
        @Schema(description = "어학", example = "OPlc, TOEPL")
        private String english;
        @Schema(description = "어학 점수", example = "AL, 955")
        private String score;
        @Schema(description = "자격증", example = "컴퓨터활용능력: 2급")
        private String certification;
        @Schema(description = "학교", example = "학교명")
        private String university;
        @Schema(description = "학과", example = "학과명")
        private String major;
        @Schema(description = "전공학점", example = "4.0/4.5")
        private String gpa;
        @Schema(description = "대외 활동")
        private List<ActivityDto> activity;
        @Schema(description = "보유 스킬", example = "Figma, Photoshop,")
        private String skill;
        @Schema(description = "링크", example = "개인 포트폴리오 url")
        private String link;
    }

    @Getter
    @Setter
    @Builder
    public static class CareerResponseDto {
        //Career 필드
        private Long id;
        private String english;
        private String score;
        private String certification;
        private String university;
        private String major;
        private String gpa;
        private List<ActivityDto> activity;
        private String skill;
        private String link;
        private String careerProfile;

        public static CareerResponseDto toDto(Career career, List<ActivityDto> activityDtoList) {

            return new CareerResponseDto(
                    career.getId(),
                    career.getEnglish(),
                    career.getScore(),
                    career.getCertification(),
                    career.getUniversity(),
                    career.getMajor(),
                    career.getGpa(),
                    activityDtoList,
                    career.getSkill(),
                    career.getLink(),
                    career.getUser().getCareerProfile()
            );
        }
    }

    @Getter
    @Setter
    @Builder
    public static class ActivityDto {
        private String name;
        private String startDate;
        private String endDate;
        private String description;

        public static ActivityDto toDto(Activity activity) {
            return ActivityDto.builder()
                    .name(activity.getName())
                    .startDate(activity.getStartDate())
                    .endDate(activity.getEndDate())
                    .description(activity.getDescription())
                    .build();
        }
    }

}
