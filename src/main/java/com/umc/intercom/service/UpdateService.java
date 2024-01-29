package com.umc.intercom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto.UserUpdateRequestDto;
import com.umc.intercom.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public void updateUser(UserUpdateRequestDto requestDto){
        User userToUpdate = userRepository.findByEmail(requestDto.getEmail())
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        userToUpdate.setPassword(passwordEncoder.encode(userToUpdate.getPassword()));
        userToUpdate.setName(requestDto.getName());
        userToUpdate.setNickname(requestDto.getNickname());
        userToUpdate.setPhone(requestDto.getPhone());
        userToUpdate.setGender(requestDto.getGender());
        userToUpdate.setBirthday(requestDto.getBirthday());
        
        userRepository.save(userToUpdate);
    }
}
