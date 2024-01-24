package com.umc.intercom.repository;

import com.umc.intercom.domain.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRepository extends JpaRepository<Talk, Long> {
}
