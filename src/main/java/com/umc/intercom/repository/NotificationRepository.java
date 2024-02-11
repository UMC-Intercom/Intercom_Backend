package com.umc.intercom.repository;

import com.umc.intercom.domain.Notification;
import com.umc.intercom.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
}
