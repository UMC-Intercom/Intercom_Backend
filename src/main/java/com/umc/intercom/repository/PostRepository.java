package com.umc.intercom.repository;

import com.umc.intercom.domain.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.umc.intercom.domain.common.enums.PostType;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByPostTypeOrderByCreatedAtDesc(PostType postType);
}
