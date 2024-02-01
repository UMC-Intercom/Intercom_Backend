package com.umc.intercom.repository;


import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostSpec;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostSpecRepository extends JpaRepository<PostSpec, Long> {
    List<PostSpec> findByPostIdIn(List<Long> postIds);
    Optional<PostSpec> findByPost(Post post);
}
