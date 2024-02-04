package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import com.umc.intercom.domain.common.enums.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Talk extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private String category;

    @Column(name = "image_url")
    private String imageUrl;

    @Setter
    @ColumnDefault("0")
    @Column(name = "view_count")
    private int viewCount;

    @Setter
    @ColumnDefault("0")
    @Column(name = "like_count")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(mappedBy = "talk", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();
}