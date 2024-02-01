package com.umc.intercom.repository;

import com.umc.intercom.domain.Career;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CareerRepository extends JpaRepository<Career, Long> {
}
