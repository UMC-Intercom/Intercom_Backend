package com.umc.intercom.dto;

import com.umc.intercom.domain.Career;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

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
        @Schema(description = "회사", example = "회사명")
        private String company;
        @Schema(description = "직급", example = "직급명")
        private String position;
        @Schema(description = "주요직무", example = "직무명, 모바일 디자인, UX UI 기획,")
        private String job;
        @Schema(description = "연봉", example = "3000")
        private String salary;
        @Schema(description = "입사 날짜", example = "2024-01-01")
        private LocalDate startDate;
        @Schema(description = "퇴사 날짜", example = "2024-02-04")
        private LocalDate endDate;
        @Schema(description = "대외 활동", example = "대외활동1, 대외활동2,")
        private String activity;
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
        private String skill;
        private String activity;
        private String link;

        public static CareerResponseDto toDto(Career career) {
            return new CareerResponseDto(
                    career.getId(),
                    career.getEnglish(),
                    career.getScore(),
                    career.getCertification(),
                    career.getUniversity(),
                    career.getMajor(),
                    career.getGpa(),
                    career.getSkill(),
                    career.getActivity(),
                    career.getLink()
            );
        }
    }
}
