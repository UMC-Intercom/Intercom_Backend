package com.umc.intercom.repository;

import com.umc.intercom.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByPostId(Long postId);
}
