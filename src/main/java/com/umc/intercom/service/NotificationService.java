package com.umc.intercom.service;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.domain.Notification;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.NotificationDto;
import com.umc.intercom.repository.NotificationRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationDto.NotificationResponseDto> getNotificationsByUser() {
        String userEmail = SecurityUtil.getCurrentUsername();
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        List<Notification> notifications = notificationRepository.findByUser(user.get());

        return notifications.stream()
                .map(notification -> {
                    NotificationDto.NotificationResponseDto.NotificationResponseDtoBuilder builder =
                            NotificationDto.NotificationResponseDto.builder()
                                    .id(notification.getId())
                                    .isRead(notification.isRead());

                    if (notification.getComment() != null) {
                        // 댓글 알림인 경우
                        builder.writer(notification.getComment().getUser().getNickname())
                                .commentId(notification.getComment().getId())
                                .comment(notification.getComment().getContent())
                                .talkId(null)
                                .talkTitle(null);
                    } else if (notification.getLikeScrap() != null) {
                        // 좋아요 알림인 경우
                        builder.writer(notification.getLikeScrap().getUser().getNickname())
                                .commentId(null)
                                .comment(null)
                                .talkId(notification.getLikeScrap().getTalk().getId())
                                .talkTitle(notification.getLikeScrap().getTalk().getTitle());
                    }

                    return builder.build();
                })
                .collect(Collectors.toList());
    }
}
