package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 15)
    private String university;

    @Column(nullable = false)
    private String major;

    @Column(nullable = false)
    private String skill;

    @Column(nullable = false)
    private String title;

    @Lob //body 필드 매우 큰 텍스트 정보에 유리
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToOne(mappedBy = "career")
    private Spec spec;



}
