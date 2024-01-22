package com.umc.intercom.controller;

import com.umc.intercom.domain.User;
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
    public ResponseEntity<Long> signUp(@RequestBody User user) {
        try {
            Long userId = signUpService.join(user);
            return ResponseEntity.ok(userId);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}



import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SignUpController {
}
