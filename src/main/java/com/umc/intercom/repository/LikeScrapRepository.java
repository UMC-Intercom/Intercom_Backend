package com.umc.intercom.repository;

import com.umc.intercom.domain.*;
import com.umc.intercom.domain.common.enums.LikeScrapType;
import com.umc.intercom.domain.common.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeScrapRepository extends JpaRepository<LikeScrap, Long> {
    Optional<LikeScrap> findByUserAndTalkAndPostTypeAndLikeScrapType(User user, Talk talk, PostType postType, LikeScrapType likeScrapType);

    Optional<LikeScrap> findByUserAndPostAndPostTypeAndLikeScrapType(User user, Post post, PostType postType, LikeScrapType likeScrapType);

    Page<LikeScrap> findByUserAndLikeScrapTypeAndPostType(User user, LikeScrapType likeScrapType, PostType postType, Pageable pageable);

    long countByUserAndLikeScrapType(User user, LikeScrapType likeScrapType);

    Optional<LikeScrap> findByUserAndJobAndPostTypeAndLikeScrapType(User user, Job job, PostType postType, LikeScrapType likeScrapType);
}
