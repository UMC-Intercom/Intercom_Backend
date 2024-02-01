package com.umc.intercom.repository;


import com.umc.intercom.domain.PostSpec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostSpecRepository extends JpaRepository<PostSpec, Long> {
    List<PostSpec> findByPostIdIn(List<Long> postIds);
}
