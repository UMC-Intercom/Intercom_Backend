package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Lob //body 필드 매우 큰 텍스트 정보에 유리
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean is_adopted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
