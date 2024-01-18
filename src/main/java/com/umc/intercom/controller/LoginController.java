package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class LoginController {
    private final  LoginService loginService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginSuccessDto> login(@RequestBody UserDto.UserLoginRequestDto userLoginRequestDto) {
        String token = loginService.login(userLoginRequestDto);
        UserDto.LoginSuccessDto loginSuccessDto = UserDto.LoginSuccessDto.builder().token(token).build();
        return ResponseEntity.ok(loginSuccessDto);
    }

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }

    // 현재 로그인된 사용자의 정보 가져오기
//    @GetMapping("/current-user")
//    public ResponseEntity<UserDto.CurrentUserDto> getCurrentUser(Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserDto.CurrentUserDto currentUserDto = extractCurrentUserDtoFromAuthentication(authentication);
//            return ResponseEntity.ok(currentUserDto);
//        } else {
//            // 인증되지 않은 사용자 또는 로그인되지 않은 상태
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//    }
//
//    private UserDto.CurrentUserDto extractCurrentUserDtoFromAuthentication(Authentication authentication) {
//        // Authentication 객체에서 필요한 정보를 추출하여 CurrentUserDto를 생성합니다.
//        String username = authentication.getName();
//        List<String> authorities = authentication.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//
//        return UserDto.CurrentUserDto.builder()
//                .username(username)
//                .authorities(authorities)
//                .build();
//    }

}
