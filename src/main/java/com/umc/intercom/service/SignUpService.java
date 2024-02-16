package com.umc.intercom.service;

import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.Gender;
import com.umc.intercom.dto.UserDto;
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
    public String join(UserDto.SignUpRequestDto requestDto) {
        // 이메일 중복 검사
        if (checkEmailDuplicate(requestDto.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        // 닉네임 중복 검사
        if (checkNicknameDuplicate(requestDto.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

        // 비밀번호 유효성 검사
        if (!isValidPassword(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호는 영문 대문자, 소문자, 숫자, 특수문자를 포함한 8~20자여야 합니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

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

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .name(requestDto.getName())
                .nickname(requestDto.getNickname())
                .birthday(requestDto.getBirthday())
                .phone(requestDto.getPhone())
                .gender(gender)
                .build();

        User newUser = userRepository.save(user);
        return newUser.getNickname();
    }

    public Boolean checkEmailDuplicate(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.isPresent(); //이미 있으면 true
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        Optional<User> user = userRepository.findByNickname(nickname);
        return user.isPresent(); //이미 있으면 true
    }

    // 비밀번호 검사 메소드
    public Boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*+=?-_])(?=\\S+$).{8,20}$";
        return password.matches(passwordPattern);
    }
}