package com.umc.intercom.repository;

import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.CareerDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CareerDetailRepository extends JpaRepository<CareerDetail, Long> {
    Optional<CareerDetail> findByCareer(Career career);
}
