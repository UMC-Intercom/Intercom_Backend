package com.umc.intercom.repository;

import com.umc.intercom.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    boolean existsByJobId(String jobId);

    Page<Job> findAllByJobMidCodeContaining(String interest, Pageable pageable);
}
