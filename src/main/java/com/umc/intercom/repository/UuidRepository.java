package com.umc.intercom.repository;

import com.umc.intercom.domain.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {
}
