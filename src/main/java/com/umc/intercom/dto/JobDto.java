package com.umc.intercom.dto;

import com.umc.intercom.domain.Job;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

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
        private boolean isScraped;  // 스크랩 여부

        public static JobDto.JobListResponseDto toScrapListDto(Job job) {
            return JobListResponseDto.builder()
                    .id(job.getId())
                    .company(job.getCompany())
                    .title(job.getTitle())
                    .viewCount(job.getViewCount())
                    .expirationDate(job.getExpirationDate())
                    .build();
        }

        public static Page<JobListResponseDto> toDtoPageWithScrap(Page<Job> jobPage, List<Long> userScrapedJobIds) {
            return jobPage.map(job -> {
                boolean isScraped = userScrapedJobIds.contains(job.getId());
                return new JobDto.JobListResponseDto(job.getId(), job.getCompany(), job.getTitle(), job.getViewCount(), job.getExpirationDate(), isScraped);
            });
        }
    }
}
