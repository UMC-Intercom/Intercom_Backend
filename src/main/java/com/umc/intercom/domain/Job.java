package com.umc.intercom.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_id")
    private String jobId;   // 공고 id

    private String url;     // 사람인 공고 링크

    private String company; // 회사명

    private String title;  // 공고 제목

    private String industry;    // 업종명

    private String location;    // 지역명

    @Column(name = "job_mid_code")
    private String jobMidCode;  // 상위 직무명

    @Column(name = "job_code", columnDefinition = "TEXT")
    private String jobCode;     // 직무명

    @Column(name = "experience_level")
    private String experienceLevel; // 경력

    @Column(name = "education_level")
    private String educationLevel;  // 학력

    private String keyword;     // 키워드

    private String salary;      // 연봉

    @Column(name = "posting_date")
    private LocalDate postingDate;  // 공고 게시일

    @Column(name = "modification_date")
    private LocalDate modificationDate;  // 공고 수정일

    @Column(name = "opening_date")
    private LocalDate openingDate;  // 모집 시작일

    @Column(name = "expiration_date")
    private LocalDate expirationDate;   // 모집 마감일

    @Column(name = "close_type")
    private String closeType;   // 마감일 형식

    @Setter
    @ColumnDefault("0")
    @Column(name = "view_count")
    private int viewCount;

    @Setter
    @ColumnDefault("0")
    @Column(name = "scrap_count")
    private int scrapCount;
}
