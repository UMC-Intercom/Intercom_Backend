package com.umc.intercom.service;

import com.umc.intercom.domain.common.enums.Gender;
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
    
    public void updateUser(String userEmail, UserUpdateRequestDto requestDto){
        User userToUpdate = userRepository.findByEmail(userEmail)
                            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 성별
        Gender gender;
        if (requestDto.getGender().equals("male")) {
            gender = Gender.MALE;
        }
        else if (requestDto.getGender().equals("female")) {
            gender = Gender.FEMALE;
        }
        else {  // (requestDto.getGender().equals("no-selected")
            gender = Gender.NONE;
        }

        userToUpdate.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        userToUpdate.setName(requestDto.getName());
        userToUpdate.setNickname(requestDto.getNickname());
        userToUpdate.setPhone(requestDto.getPhone());
        userToUpdate.setGender(gender);
        userToUpdate.setBirthday(requestDto.getBirthday());

        userRepository.save(userToUpdate);
    }
}
