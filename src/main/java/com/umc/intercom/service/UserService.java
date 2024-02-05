package com.umc.intercom.service;

import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto.FindEmailResponseDto findEmailByPhone(UserDto.FindEmailRequestDto requestDto) {
        User user = userRepository.findUserByPhone(requestDto.getPhone());
        return new UserDto.FindEmailResponseDto(user.getName(), user.getEmail());
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updatePassword(String email, String newPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
        }
    }
}
