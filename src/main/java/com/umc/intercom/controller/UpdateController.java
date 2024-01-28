package com.umc.intercom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.umc.intercom.dto.UserDto.UserUpdateRequestDto;
import com.umc.intercom.service.UpdateService;

import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/users")
public class UpdateController {
    
    private final UpdateService updateService;
    
    @Autowired
    public UpdateController(UpdateService updateService){
        this.updateService = updateService;
    }
    
    @PutMapping("/update")
    public ResponseEntity<Void> updateUser(@RequestBody UserUpdateRequestDto requestDto) {
        updateService.updateUser(requestDto);
        return ResponseEntity.noContent().build();
    }
    
}