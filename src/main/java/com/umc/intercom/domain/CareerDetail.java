package com.umc.intercom.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CareerDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "career_id")
    private Career career;

    private String company;

    private String position;

    private Long salary;

    private String job;

    @Column(name = "start_date")
    private LocalDate startDate;    // LocalDate는 YYYY-MM-DD 형식

    @Column(name = "end_date")
    private LocalDate endDate;
}
