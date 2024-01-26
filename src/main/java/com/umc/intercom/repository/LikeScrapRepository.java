package com.umc.intercom.repository;

import com.umc.intercom.domain.LikeScrap;
import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.PostType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeScrapRepository extends JpaRepository<LikeScrap, Long> {
    Optional<LikeScrap> findByUserAndTalkAndPostType(User user, Talk talk, PostType postType);
}
