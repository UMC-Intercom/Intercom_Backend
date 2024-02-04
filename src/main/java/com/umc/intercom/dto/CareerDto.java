package com.umc.intercom.dto;

import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.CareerDetail;
import com.umc.intercom.domain.Spec;
import lombok.*;

import java.time.LocalDate;

@Data
public class CareerDto {

    @Getter
    public static class CareerRequestDto {
        //Career 필드
        private String university;
        private String major;
        private String skill;
        private String title;
        private String content;
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
                    spec.getGpa()
            );
        }
    }

}
