package com.umc.intercom.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umc.intercom.dto.UserDto.UserUpdateRequestDto;
import com.umc.intercom.service.UpdateService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UpdateController {
    
    private final UpdateService updateService;

    @Operation(summary = "회원 정보 수정")
    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateRequestDto requestDto) {
        updateService.updateUser(requestDto);
        return ResponseEntity.noContent().build();
    }
    
}