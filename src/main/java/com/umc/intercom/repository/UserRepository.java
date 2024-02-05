package com.umc.intercom.repository;

import com.umc.intercom.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    User findUserByPhone(String phone);
}