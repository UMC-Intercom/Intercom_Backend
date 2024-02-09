package com.umc.intercom.dto;

import com.umc.intercom.domain.Job;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

@Data
public class JobDto {

    @Getter
    @Builder
    public static class ScrapResponseDto {
        private Long id;
        private String company;
        private String title;
        private int viewCount;
        private LocalDate expirationDate;

        public static Page<JobDto.ScrapResponseDto> toDtoPage(Page<Job> jobPage) {
            return jobPage.map(job -> new JobDto.ScrapResponseDto(job.getId(), job.getCompany(), job.getTitle(), job.getViewCount(), job.getExpirationDate()));
        }

        public static JobDto.ScrapResponseDto toScrapListDto(Job job) {
            return ScrapResponseDto.builder()
                    .id(job.getId())
                    .company(job.getCompany())
                    .title(job.getTitle())
                    .viewCount(job.getViewCount())
                    .expirationDate(job.getExpirationDate())
                    .build();
        }
    }
}
