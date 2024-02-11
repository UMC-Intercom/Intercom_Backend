package com.umc.intercom.controller;

import com.umc.intercom.dto.NotificationDto;
import com.umc.intercom.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/noti")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "알림 내역 조회")
    @GetMapping
    public ResponseEntity<List<NotificationDto.NotificationResponseDto>> getAllNotifications() {
        List<NotificationDto.NotificationResponseDto> notificationRequestDto = notificationService.getNotificationsByUser();
        return ResponseEntity.ok(notificationRequestDto);
    }
}
