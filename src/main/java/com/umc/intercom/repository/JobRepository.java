package com.umc.intercom.repository;

import com.umc.intercom.domain.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    boolean existsByJobId(String jobId);

    Page<Job> findAllByJobMidCodeContaining(String interest, Pageable pageable);

    Page<Job> findByJobMidCodeContainingAndLocationContaining(String jobMidCode, String location, Pageable pageable);

    Page<Job> findByJobMidCodeContainingAndLocationContainingAndTitleContaining(String jobMidCode, String location, String keyword, Pageable pageable);

    Page<Job> findByJobMidCodeContaining(String jobMidCode, Pageable pageable);

    Page<Job> findByJobMidCodeContainingAndTitleContaining(String jobMidCode, String keyword, Pageable pageable);
}