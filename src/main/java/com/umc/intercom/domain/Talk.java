package com.umc.intercom.domain;

import com.umc.intercom.domain.common.BaseEntity;
import com.umc.intercom.domain.common.enums.Status;
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
    @Column(nullable = false)
    private String content;

    private String category;

    @ElementCollection
    @CollectionTable(name = "talk_images", joinColumns = @JoinColumn(name = "talk_id"))
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Setter
    @ColumnDefault("0")
    @Column(name = "view_count")
    private int viewCount;

    @Setter
    @ColumnDefault("0")
    @Column(name = "like_count")
    private int likeCount;

    @Setter
    @ColumnDefault("0")
    @Column(name = "scrap_count")
    private int scrapCount;

    @Setter
    @ColumnDefault("0")
    private int commentCount;   // 댓글 수

    @Setter
    @ColumnDefault("0")
    private int replyCount;     // 대댓글 수

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @OneToMany(mappedBy = "talk", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;  // 저장 상태

    public void update(String title, String content, String category, List<String> pictureUrls, Status status) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.imageUrls = pictureUrls;
        this.status = status;
    }
}