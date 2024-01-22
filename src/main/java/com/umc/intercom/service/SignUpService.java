package com.umc.intercom.service;

import com.umc.intercom.domain.User;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SignUpService {
    private final UserRepository userRepository;

    public SignUpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //회원가입
    public Long join(User user) {
        //같은 이름 중복 회원 X
        validateDuplicateMemberEmail(user); // 중복 회원 검증 이메일
        validateDuplicateMemberNickName(user); // 중복 회원 검증 닉네임
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicateMemberEmail(User user) {
        userRepository.findByEmail(user.getUsername())
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
