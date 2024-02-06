package com.umc.intercom.controller;


import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.service.WithdrawService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class WithdrawController {
    private final WithdrawService withdrawService;

    @Autowired
    public WithdrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @Operation(summary = "회원 탈퇴", description = "최종 탈퇴")
    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw() {
        String userEmail = SecurityUtil.getCurrentUsername();
        withdrawService.withdraw(userEmail);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원 탈퇴 전 본인 인증", description = "올바른 비밀번호를 입력해야 탈퇴 가능")
    @PostMapping("/withdraw/validate")
    public ResponseEntity<Void> validateBeforeWithdraw(@RequestBody UserDto.ValidateRequestDto requestDto) {
        String userEmail = SecurityUtil.getCurrentUsername();
        withdrawService.validateBeforeWithdraw(userEmail, requestDto.getPassword());
        return ResponseEntity.noContent().build();
    }
}

