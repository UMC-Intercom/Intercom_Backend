package com.umc.intercom.dto;

import com.umc.intercom.domain.Job;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

@Data
public class JobDto {

    @Getter
    @Builder
    public static class JobListResponseDto {
        private Long id;
        private String company;
        private String title;
        private int viewCount;
        private LocalDate expirationDate;

        public static Page<JobDto.JobListResponseDto> toDtoPage(Page<Job> jobPage) {
            return jobPage.map(job -> new JobDto.JobListResponseDto(job.getId(), job.getCompany(), job.getTitle(), job.getViewCount(), job.getExpirationDate()));
        }

        public static JobDto.JobListResponseDto toScrapListDto(Job job) {
            return JobListResponseDto.builder()
                    .id(job.getId())
                    .company(job.getCompany())
                    .title(job.getTitle())
                    .viewCount(job.getViewCount())
                    .expirationDate(job.getExpirationDate())
                    .build();
        }
    }
}
