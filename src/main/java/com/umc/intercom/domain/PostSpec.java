package com.umc.intercom.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostSpec {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id", referencedColumnName = "id")
    private Post post;

    @Column(nullable = false)
    private String education;   // 학력

    @Column(nullable = false)
    private String major;   // 학과

    @Column(nullable = false)
    private String gpa;     // 학점

    @Column(nullable = false)
    private String activity;    // 대외활동

    @Column(nullable = false)
    private String certification;   // 자격증

    @Column(nullable = false)
    private String english;     // 어학 종류

    @Column(nullable = false)
    private String score;     // 어학 취득 점수
}
