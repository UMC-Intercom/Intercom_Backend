package com.umc.intercom.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    private String english; //어학

    private String score; //어학 획득 점수

    private String certification; //자격증

    private String university; //학교

    private String major; //학과

    @Column(name = "graduate_status")
    private String graduateStatus;  // 졸업 여부
    
    private String gpa; //전공 학점

    private String skill; //보유 스펙 ex)일러스트, 포토샵

    private String link; //링크

    @OneToMany(mappedBy = "career", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();
}
