package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.config.security.user.UserDetailsImpl;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.repository.UserRepository;
import com.umc.intercom.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class LoginController {
    
    private final LoginService loginService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginSuccessDto> login(@RequestBody UserDto.UserLoginRequestDto userLoginRequestDto) {
        String token = loginService.login(userLoginRequestDto);
        UserDto.LoginSuccessDto loginSuccessDto = UserDto.LoginSuccessDto.builder().token(token).build();
        return ResponseEntity.ok(loginSuccessDto);
    }
    
    // 현재 로그인한 사용자 정보 조회
    @GetMapping("/current-user")
    public ResponseEntity<UserDto.CurrentUserDto> getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Optional<User> user = userRepository.findByEmail(username); // Replace with your actual repository method

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserDto.CurrentUserDto currentUserDto = UserDto.CurrentUserDto.builder()
//                .username(userDetails.getUsername())
                .email(userDetails.getUsername())
                .name(user.get().getName())
                .nickname(user.get().getNickname())
                .authorities(authorities)
                .build();

        System.out.println("현재 유저 이메일: " + username);

        return ResponseEntity.ok(currentUserDto);
    }

}
