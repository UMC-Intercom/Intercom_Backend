package com.umc.intercom.repository;

import com.umc.intercom.domain.Activity;
import com.umc.intercom.domain.Career;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByCareer(Career careerToUpdate);
}
