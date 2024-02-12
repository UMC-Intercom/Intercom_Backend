package com.umc.intercom.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String english; //어학

    @Column(nullable = false)
    private String score; //어학 획득 점수

    @Column(nullable = false)
    private String certification; //자격증

    @Column(nullable = false)
    private String university; //학교

    @Column(nullable = false)
    private String major; //학과

    @Column(nullable = false)
    private String gpa; //전공 학점

    @Column(nullable = false)
    private String activity; //대외활동

    @Column(nullable = false)
    private String skill; //보유 스펙 ex)일러스트, 포토샵

    @Column(nullable = false)
    private String link; //링크
}