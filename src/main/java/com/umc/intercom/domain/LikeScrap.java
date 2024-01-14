package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import com.umc.intercom.domain.common.enums.TableType;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

public class LikeScrap extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scrapId;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private LikeScrap likeScrap;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private TableType tableType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
