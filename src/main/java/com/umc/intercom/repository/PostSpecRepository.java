package com.umc.intercom.repository;


import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostSpec;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostSpecRepository extends JpaRepository<PostSpec, Long> {
    Optional<PostSpec> findByPost(Post post);
}
