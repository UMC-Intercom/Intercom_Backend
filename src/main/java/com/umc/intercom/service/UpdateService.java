package com.umc.intercom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto.UserUpdateRequestDto;
import com.umc.intercom.repository.UserRepository;

@Service
public class UpdateService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UpdateService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    
    public void updateUser(UserUpdateRequestDto requestDto){
        User userToUpdate = userRepository.findByEmail(requestDto.getEmail())
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        userToUpdate.setPassword(requestDto.getPassword());
        userToUpdate.setName(requestDto.getName());
        userToUpdate.setNickname(requestDto.getNickname());
        userToUpdate.setPhone(requestDto.getPhone());
        userToUpdate.setGender(requestDto.getGender());
        userToUpdate.setBirthday(requestDto.getBirthday());
        
        userRepository.save(userToUpdate);
    }
}
