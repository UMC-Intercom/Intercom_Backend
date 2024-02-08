package com.umc.intercom.repository;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.common.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.umc.intercom.domain.common.enums.PostType;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostType(PostType postType, Pageable pageable);
    List<Post> findByPostTypeOrderByCreatedAtDesc(PostType postType);

    List<Post> findByCompanyAndDepartmentAndPostType(String company, String department, PostType postType);
}
