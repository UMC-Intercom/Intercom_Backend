package com.umc.intercom.repository;

import com.umc.intercom.domain.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    boolean existsByJobId(String jobId);
}
