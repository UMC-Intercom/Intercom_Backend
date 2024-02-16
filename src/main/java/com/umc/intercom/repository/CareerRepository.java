package com.umc.intercom.repository;

import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CareerRepository extends JpaRepository<Career, Long> {
    Optional<Career> findByUser(User user);
}

