package com.umc.intercom.repository;

import com.umc.intercom.domain.Comment;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.AdoptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>{
    List<Comment> findAllByTalkId(Long talkId);

    long countByUser(User user);

    Optional<Comment> findByTalkIdAndAdoptionStatus(Long talkId, AdoptionStatus adoptionStatus);
}
