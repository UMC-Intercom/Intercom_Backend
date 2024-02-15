package com.umc.intercom.repository;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByPostType(PostType postType, Pageable pageable);

    Page<Post> findByUserAndPostType(User user, PostType postType, Pageable pageable);

    Page<Post> findByPostTypeAndDepartmentContaining(PostType postType, String department, Pageable pageable);

    Page<Post> findByDepartmentContainingAndPostType(String department, PostType postType, Pageable pageable);

    Page<Post> findByCompanyContainingAndPostType(String company, PostType postType, Pageable pageable);

    Page<Post> findByCompanyContainingAndDepartmentContainingAndPostType(String company, String department, PostType postType, Pageable pageable);
}
