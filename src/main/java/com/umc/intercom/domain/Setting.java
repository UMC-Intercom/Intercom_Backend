package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;

public class Setting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long settingId;

    @Lob //body 필드 매우 큰 텍스트 정보에 유리
    @Column(nullable = false, columnDefinition = "TEXT")
    private String siteExplanation;

    @Lob //body 필드 매우 큰 텍스트 정보에 유리
    @Column(nullable = false, columnDefinition = "TEXT")
    private String temrsOfUse;

    private LocalDate updateDate;

    //accountWithDrawal 계정탈퇴
    //logout 로그아웃
    //IA에 있던데 일단 보류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
