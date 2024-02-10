package com.umc.intercom.dto;

import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.CareerDetail;
import com.umc.intercom.domain.Spec;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Data
public class CareerDto {

    @Getter
    public static class CareerRequestDto {
        //Career 필드
        @Schema(description = "학교", example = "학교명")
        private String university;
        @Schema(description = "학과", example = "학과명")
        private String major;
        @Schema(description = "보유 스킬", example = "Figma, Photoshop,")
        private String skill;
        @Schema(description = "자소서 제목", example = "자소서 제목")
        private String title;
        @Schema(description = "자소서 내용", example = "자소서 내용")
        private String content;
        //CareerDetail 필드
        @Schema(description = "회사", example = "회사명")
        private String company;
        @Schema(description = "직급", example = "직급명")
        private String position;
        @Schema(description = "연봉", example = "3000")
        private Long salary;
        @Schema(description = "주요직무", example = "직무명, 모바일 디자인, UX UI 기획,")
        private String job;
        @Schema(description = "입사 날짜", example = "2024-01-01")
        private LocalDate startDate;
        @Schema(description = "퇴사 날짜", example = "2024-02-04")
        private LocalDate endDate;
        //Spec필드
        @Schema(description = "자격증", example = "자격증1, 자격증2,")
        private String certification;
        @Schema(description = "대외 활동", example = "대외활동1, 대외활동2,")
        private String activity;
        @Schema(description = "수상 내역", example = "수상내역1, 수상내역2,")
        private String award;
        @Schema(description = "해외 경험", example = "해외경험1, 해외경험2,")
        private String globalExp;
        @Schema(description = "봉사활동", example = "봉사활동1, 봉사황동2,")
        private String volunteer;
        @Schema(description = "학점", example = "4.0/4.5")
        private String gpa;
    }

    @Getter
    @Setter
    @Builder
    public static class CareerResponseDto {
        //Career 필드
        private Long id;
        private String university;
        private String major;
        private String skill;
        private String title;
        private String content;
        private String writer;
        //CareerDetail 필드
        private String company;
        private String position;
        private Long salary;
        private String job;
        private LocalDate startDate;
        private LocalDate endDate;
        //Spec필드
        private String certification;
        private String activity;
        private String award;
        private String globalExp;
        private String volunteer;
        private String gpa;

        private String careerProfile;   // 커리어 프로필 url

        public static CareerResponseDto toDto(Career career, CareerDetail careerDetail, Spec spec) {
            return new CareerResponseDto(
                    career.getId(),
                    career.getUniversity(),
                    career.getMajor(),
                    career.getSkill(),
                    career.getTitle(),
                    career.getContent(),
                    career.getUser().getNickname(),
                    careerDetail.getCompany(),
                    careerDetail.getPosition(),
                    careerDetail.getSalary(),
                    careerDetail.getJob(),
                    careerDetail.getStartDate(),
                    careerDetail.getEndDate(),
                    spec.getCertification(),
                    spec.getActivity(),
                    spec.getAward(),
                    spec.getGlobalExp(),
                    spec.getVolunteer(),
                    spec.getGpa(),
                    career.getUser().getCareerProfile()
            );
        }
    }

}
