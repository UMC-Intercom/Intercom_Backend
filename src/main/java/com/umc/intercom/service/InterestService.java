package com.umc.intercom.service;

import com.umc.intercom.domain.User;
import com.umc.intercom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final UserRepository userRepository;

    @Transactional
    public void addInterestsByNickname(String nickname, List<String> interests) {
        Optional<User> user = userRepository.findByNickname(nickname);

        if (user.isPresent()) {
            user.get().setInterests(interests);
            userRepository.save(user.get());
        } else {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }
    }

    public List<String> getInterests(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return user.getInterests();
    }

    @Transactional
    public void updateInterests(String userEmail, List<String> interests) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        user.setInterests(interests);
        userRepository.save(user);
    }

    public boolean hasInterests(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return !user.getInterests().isEmpty();
    }

}
