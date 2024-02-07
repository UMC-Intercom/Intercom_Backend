package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Spec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="career_id", referencedColumnName = "id")
    private Career career;

    @Column(nullable = false)
    private String certification;

    @Column(nullable = false)
    private String activity;

    @Column(nullable = false)
    private String award;

    @Column(nullable = false, name = "global_exp")
    private String globalExp;

    @Column(nullable = false)
    private String volunteer;

    @Column(nullable = false)
    private String gpa;
}
