package com.umc.intercom.service;

import com.umc.intercom.domain.User;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //회원가입
    public Long join(User user) {
        // 이메일 중복 검사
        if (checkEmailDuplicate(user.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 검사
        if (checkNicknameDuplicate(user.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userRepository.save(user);
        return user.getId();
    }

    public Boolean checkEmailDuplicate(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent(); //이미 있으면 true
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        return user.isPresent(); //이미 있으면 true
    }
}