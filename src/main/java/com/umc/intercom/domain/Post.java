package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import com.umc.intercom.domain.common.enums.Category;
import com.umc.intercom.domain.common.enums.TableType;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false, length = 30)
    private String title;

    @Lob //body 필드 매우 큰 텍스트 정보에 유리
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(30)")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(10)")
    private TableType tableType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String image_url;

    private int view_count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
