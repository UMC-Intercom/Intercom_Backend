package com.umc.intercom.controller;

import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.service.SignUpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class SignUpController {
    private final SignUpService signUpService;

    public SignUpController(SignUpService signUpService) {
        this.signUpService = signUpService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDto.SignUpRequestDto signUpRequestDto) {
        try {
            String userNickname = signUpService.join(signUpRequestDto);
            return ResponseEntity.ok(userNickname);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/signup/email")
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestBody String email) {
        try {
            Boolean isDuplicate = signUpService.checkEmailDuplicate(email);
            return ResponseEntity.ok(isDuplicate);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/signup/nickname")
    public ResponseEntity<Boolean> nicknameValidateDuplicate(@RequestBody String nickname) {
        try {
            Boolean isDuplicate = signUpService.checkNicknameDuplicate(nickname);
            return ResponseEntity.ok(isDuplicate);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}