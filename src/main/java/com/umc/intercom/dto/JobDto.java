package com.umc.intercom.dto;

import com.umc.intercom.domain.Company;
import com.umc.intercom.domain.Job;
import com.umc.intercom.repository.CompanyRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Data
public class JobDto {
    @Getter
    @Builder
    public static class JobListResponseDto {
        private Long id;
        @Schema(description = "회사명")
        private String company;
        @Schema(description = "제목")
        private String title;
        @Schema(description = "조회수")
        private int viewCount;
        @Schema(description = "마감일")
        private LocalDate expirationDate;
        @Schema(description = "스크랩 여부")
        private boolean isScraped;
        @Schema(description = "기업 로고 이미지 url")
        private String logoUrl;

        public static JobDto.JobListResponseDto toScrapListDto(Job job) {
            return JobListResponseDto.builder()
                    .id(job.getId())
                    .company(job.getCompany())
                    .title(job.getTitle())
                    .viewCount(job.getViewCount())
                    .expirationDate(job.getExpirationDate())
                    .build();
        }

        public static Page<JobListResponseDto> toDtoPageWithScrap(Page<Job> jobPage, List<Long> userScrapedJobIds, CompanyRepository companyRepository) {
            return jobPage.map(job -> {
                boolean isScraped = userScrapedJobIds.contains(job.getId());
                Optional<Company> company = companyRepository.findByName(job.getCompany());
                String logoUrl = (company.isPresent()) ? company.get().getLogoUrl() : null;

                return new JobDto.JobListResponseDto(job.getId(), job.getCompany(), job.getTitle(), job.getViewCount(), job.getExpirationDate(), isScraped, logoUrl);
            });
        }
    }

    @Getter
    @Builder
    public static class JobDetailsResponseDto{
        private Long id;
        private String jobId;
        private String url;
        private String company;
        private String title;
        private String industry;
        private String location;
        private String jobMidCode;
        private String jobCode;
        private String experienceLevel;
        private String educationLevel;
        private String keyword;
        private String salary;
        private LocalDate postingDate;
        private LocalDate modificationDate;
        private LocalDate openingDate;
        private LocalDate expirationDate;
        private String closeType;
        private int viewCount;
    }
}
