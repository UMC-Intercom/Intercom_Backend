package com.umc.intercom.repository;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostDetailRepository extends JpaRepository<PostDetail, Long> {
    List<PostDetail> findByPostIdIn(List<Long> postIds);
    Optional<PostDetail> findByPost(Post post);
}
