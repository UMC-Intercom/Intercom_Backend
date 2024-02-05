package com.umc.intercom.repository;

import com.umc.intercom.domain.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TalkRepository extends JpaRepository<Talk, Long> {
    Page<Talk> findByTitleContaining(String title, Pageable pageable);

    @Query(value = "SELECT t, COUNT(c) as commentCount FROM Talk t LEFT JOIN t.comments c GROUP BY t ORDER BY commentCount DESC")
    Page<Object[]> findTalksWithCommentCounts(Pageable pageable);
}
