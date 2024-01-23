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
        //같은 이름 중복 회원 X
//        validateDuplicateMemberEmail(user); // 중복 회원 검증 이메일
//        validateDuplicateMemberNickName(user); // 중복 회원 검증 닉네임

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userRepository.save(user);
        return user.getId();
    }

    public Boolean checkEmailDuplicate(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent();
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        return user.isPresent();
    }

    private void validateDuplicateMemberEmail(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 이메일입니다.");
                });
    }

    private void validateDuplicateMemberNickName(User user) {
        userRepository.findByNickname(user.getNickname())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 닉네임입니다.");
                });
    }
}