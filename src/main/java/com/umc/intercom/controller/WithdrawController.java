package com.umc.intercom.controller;


import com.umc.intercom.dto.UserDto;
import com.umc.intercom.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class WithdrawController {
    private final WithdrawService withdrawService;

    @Autowired
    public WithdrawController(WithdrawService withdrawService) {
        this.withdrawService = withdrawService;
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody UserDto.UserRequestDto requestDto) {
        withdrawService.withdraw(requestDto);
        return ResponseEntity.noContent().build();
    }
}

