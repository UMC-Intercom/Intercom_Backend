package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 이메일
    @Operation(summary = "이메일 찾기", description = "가입 시 등록한 휴대폰 번호로 이름, 이메일 찾기")
    @PostMapping("/find-email")
    public ResponseEntity<UserDto.FindEmailResponseDto> findEmailByPhone(@RequestBody UserDto.FindEmailRequestDto requestDto) {
        UserDto.FindEmailResponseDto responseDto = userService.findEmailByPhone(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 비밀번호
    @Operation(summary = "이메일 조회", description = "비밀번호 재설정 전 이메일이 존재하는지 확인")
    @PostMapping("/check-email")
    public ResponseEntity<Void> checkEmail(@RequestBody UserDto.EmailRequestDto requestDto) {
        String email = requestDto.getEmail();
        Optional<User> user = userService.findUserByEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "비밀번호 재설정", description = "이메일과 새로운 비밀번호로 비밀번호 재설정")
    @PutMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody UserDto.UserRequestDto requestDto) {
        String email = requestDto.getEmail();
        String newPassword = requestDto.getPassword();

        Optional<User> user = userService.findUserByEmail(email);
        if (user.isPresent()) {
            userService.updatePassword(email, newPassword);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 설정 > 프로필
    @Operation(summary = "설정 > 프로필 등록")
    @PostMapping(value = "/default-profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveProfile(@RequestPart(value = "file", required = false) MultipartFile file) {
        String userEmail = SecurityUtil.getCurrentUsername();

        String userProfile = userService.saveProfile(file, userEmail, "defaultProfile");
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    // 내 커리어 > 프로필
    @Operation(summary = "내 커리어 > 프로필 등록")
    @PostMapping(value = "/career-profile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveCareerProfile(@RequestPart(value = "file", required = false) MultipartFile file) {
        String userEmail = SecurityUtil.getCurrentUsername();

        String userProfile = userService.saveProfile(file, userEmail, "careerProfile");
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

    @Operation(summary = "설정 > 프로필 조회")
    @GetMapping(value = "/default-profile")
    public ResponseEntity<String> getProfile() {
        String userEmail = SecurityUtil.getCurrentUsername();

        String userProfile = userService.getProfile(userEmail);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }

}
