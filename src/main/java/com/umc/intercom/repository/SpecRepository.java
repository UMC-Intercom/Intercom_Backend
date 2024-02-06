package com.umc.intercom.repository;
import com.umc.intercom.domain.Career;
import com.umc.intercom.domain.Spec;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpecRepository extends JpaRepository<Spec, Long> {
    Optional<Spec> findByCareer(Career career);
}
