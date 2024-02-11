package com.umc.intercom.repository;

import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TalkRepository extends JpaRepository<Talk, Long> {
    Page<Talk> findByTitleContainingAndStatus(String title, Status status, Pageable pageable);

    @Query(value = "SELECT t, COUNT(c) as commentCount FROM Talk t LEFT JOIN t.comments c WHERE t.status = :status GROUP BY t ORDER BY commentCount DESC")
    Page<Object[]> findTalksWithCommentCounts(@Param("status") Status status, Pageable pageable);

    Page<Talk> findAllByStatus(Status status, Pageable pageable);

    Optional<Talk> findTalkByUserEmailAndStatus(String userEmail, Status status);

    Page<Talk> findByUserAndStatus(User user, Status status, Pageable pageable);
}
