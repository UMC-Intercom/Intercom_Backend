package com.umc.intercom.controller;

import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.service.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class SignUpController {
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDto.SignUpRequestDto signUpRequestDto) {
        try {
            String userNickname = signUpService.join(signUpRequestDto);
            return ResponseEntity.ok(userNickname);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "이메일 중복확인", description = "중복인 경우 true, 중복이 아닌 경우 false 반환")
    @GetMapping("/signup/email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestParam String email) {
        try {
            Boolean isDuplicate = signUpService.checkEmailDuplicate(email);
            return ResponseEntity.ok(isDuplicate);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "닉네임 중복확인", description = "중복인 경우 true, 중복이 아닌 경우 false 반환")
    @GetMapping("/signup/nickname")
    public ResponseEntity<Boolean> nicknameValidateDuplicate(@RequestParam String nickname) {
        try {
            Boolean isDuplicate = signUpService.checkNicknameDuplicate(nickname);
            return ResponseEntity.ok(isDuplicate);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}