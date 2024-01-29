package com.umc.intercom.repository;

import com.umc.intercom.domain.Talk;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRepository extends JpaRepository<Talk, Long> {
    Page<Talk> findByTitleContaining(String title, Pageable pageable);
}
