package com.umc.intercom.domain;

import com.umc.intercom.domain.common.enums.LikeScrapType;
import com.umc.intercom.domain.common.enums.PostType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class LikeScrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "like_scrap_type")
    private LikeScrapType likeScrapType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talk_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Talk talk;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;
}
