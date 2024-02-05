package com.umc.intercom.service;

import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto.FindEmailResponseDto findEmailByPhone(UserDto.FindEmailRequestDto requestDto) {
        User user = userRepository.findUserByPhone(requestDto.getPhone());
        return new UserDto.FindEmailResponseDto(user.getName(), user.getEmail());
    }
}
