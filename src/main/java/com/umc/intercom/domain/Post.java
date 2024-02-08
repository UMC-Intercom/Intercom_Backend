package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import com.umc.intercom.domain.common.enums.PostType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String company;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String year;        // 합격 연도

    @Column(nullable = false)
    private String semester;    // 상반기, 하반기

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @ColumnDefault("0")
    @Column(name = "view_count")
    private int viewCount;

    @Setter
    @ColumnDefault("0")
    @Column(name = "scrap_count")
    private int scrapCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    //@OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}