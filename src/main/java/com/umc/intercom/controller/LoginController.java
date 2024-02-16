package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.repository.UserRepository;
import com.umc.intercom.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class LoginController {

    private final LoginService loginService;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;

    // 로그인
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인")
    @PostMapping("/login")
    public ResponseEntity<UserDto.LoginSuccessDto> login(@RequestBody UserDto.UserRequestDto userRequestDto) {
        UserDto.LoginSuccessDto loginSuccessDto = loginService.login(userRequestDto);
        return ResponseEntity.ok(loginSuccessDto);
    }

    // 현재 로그인한 사용자 정보 조회
    @Operation(summary = "현재 로그인한 사용자 정보 조회")
    @GetMapping("/current-user")
    public ResponseEntity<UserDto.CurrentUserDto> getCurrentUser() {
        String username = SecurityUtil.getCurrentUsername();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Optional<User> user = userRepository.findByEmail(username);

        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        UserDto.CurrentUserDto currentUserDto = UserDto.CurrentUserDto.builder()
                .email(userDetails.getUsername())
                .name(user.get().getName())
                .nickname(user.get().getNickname())
                .phone(user.get().getPhone())
                .gender(String.valueOf(user.get().getGender()))
                .birthday(user.get().getBirthday())
                .authorities(authorities)
                .build();

        return ResponseEntity.ok(currentUserDto);
    }
}
