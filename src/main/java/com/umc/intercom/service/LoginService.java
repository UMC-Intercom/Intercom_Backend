package com.umc.intercom.service;

import com.umc.intercom.config.jwt.JwtTokenProvider;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.UserDto;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(UserDto.UserLoginRequestDto userLoginRequestDto) {
        String email = userLoginRequestDto.getEmail();
        String password = userLoginRequestDto.getPassword();

        Optional<User> user = userRepository.findByEmail(email);

        if (
                user.isEmpty() ||
                        !passwordEncoder.matches(password, user.get().getPassword())
        ) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "존재하지 않는 아이디 또는 패스워드입니다."
            );
        }

        // username으로 email 사용
        return jwtTokenProvider.createToken(user.get().getEmail(), user.get().getRole());
    }
}
