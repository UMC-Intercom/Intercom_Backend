package com.umc.intercom.service;

import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.repository.TalkRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TalkService {

    private TalkRepository talkRepository;
    private UserRepository userRepository;

    public Talk createTalk(Talk talk, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        talk.setUser(user.get());   // 작성자 설정 후 저장
        return talkRepository.save(talk);
    }

    public Page<Talk> getAllTalks(int page) {
        // 페이징 처리
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        // 페이지 배열은 0부터 시작 -> page-1,  페이지 당 데이터 개수 10개(추후 수정)
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        // jpa에서 기본적으로 제공 -> repo에 적어주지 않아도o
        return talkRepository.findAll(pageable);
    }

    public Page<Talk> getAllTalksByViewCounts(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("viewCount"));
        sorts.add(Sort.Order.desc("createdAt"));    // 조회수가 동일하면 최신순으로 정렬
        
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        return talkRepository.findAll(pageable);
    }

    public Optional<Talk> getTalkById(Long id) {
        return talkRepository.findById(id);
    }

}
