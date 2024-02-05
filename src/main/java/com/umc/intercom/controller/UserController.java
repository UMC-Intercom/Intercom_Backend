package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "이메일 찾기", description = "가입 시 등록한 휴대폰 번호로 이름, 이메일 찾기")
    @PostMapping("/find-email")
    public ResponseEntity<UserDto.FindEmailResponseDto> findEmailByPhone(@RequestBody UserDto.FindEmailRequestDto requestDto) {
        UserDto.FindEmailResponseDto responseDto = userService.findEmailByPhone(requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
