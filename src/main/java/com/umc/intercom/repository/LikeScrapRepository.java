package com.umc.intercom.repository;

import com.umc.intercom.domain.*;
import com.umc.intercom.domain.common.enums.LikeScrapType;
import com.umc.intercom.domain.common.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikeScrapRepository extends JpaRepository<LikeScrap, Long> {
    Optional<LikeScrap> findByUserAndTalkAndPostTypeAndLikeScrapType(User user, Talk talk, PostType postType, LikeScrapType likeScrapType);

    Optional<LikeScrap> findByUserAndPostAndPostTypeAndLikeScrapType(User user, Post post, PostType postType, LikeScrapType likeScrapType);

    Page<LikeScrap> findByUserAndLikeScrapTypeAndPostType(User user, LikeScrapType likeScrapType, PostType postType, Pageable pageable);

    long countByUserAndLikeScrapType(User user, LikeScrapType likeScrapType);

    Optional<LikeScrap> findByUserAndJobAndPostTypeAndLikeScrapType(User user, Job job, PostType postType, LikeScrapType likeScrapType);

    @Query("SELECT job.id FROM LikeScrap ls JOIN ls.job job WHERE ls.user.email = :userEmail AND ls.postType = :postType")
    List<Long> findJobIdsByUserEmailAndPostType(String userEmail, PostType postType);

    Optional<LikeScrap> findByUserAndCommentAndPostTypeAndLikeScrapType(User user, Comment comment, PostType postType, LikeScrapType likeScrapType);

    boolean existsByUserAndCommentAndPostType(User user, Comment comment, PostType postType);
}
