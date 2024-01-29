package com.umc.intercom.service;

import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.TalkDto;
import com.umc.intercom.repository.TalkRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

    public TalkDto createTalk(TalkDto talkDto, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        Talk talk = Talk.builder()
                .title(talkDto.getTitle())
                .content(talkDto.getContent())
                .category(talkDto.getCategory())
                .imageUrl(talkDto.getImageUrl())
                .viewCount(talkDto.getViewCount())
                .user(user.orElseThrow(() -> new RuntimeException("User not found")))
                .build();

        talk.getUser().setNickname(user.get().getNickname());

        Talk createdTalk = talkRepository.save(talk);
        return TalkDto.toDto(createdTalk);
    }

    public Page<TalkDto> getAllTalks(int page) {
        // 페이징 처리
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        // 페이지 배열은 0부터 시작 -> page-1,  페이지 당 데이터 개수 10개(추후 수정)
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        // jpa에서 기본적으로 제공 -> repo에 적어주지 않아도o
        Page<Talk> talkPage =  talkRepository.findAll(pageable);
        return TalkDto.toDtoPage(talkPage);
    }

    public Page<TalkDto> getAllTalksByViewCounts(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("viewCount"));
        sorts.add(Sort.Order.desc("createdAt"));    // 조회수가 동일하면 최신순으로 정렬
        
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Talk> talkPage = talkRepository.findAll(pageable);
        return TalkDto.toDtoPage(talkPage);
    }

    public Optional<TalkDto> getTalkById(Long id) {
        Optional<Talk> talk = talkRepository.findById(id);
        return talk.map(TalkDto::toDto);
    }

    public Page<TalkDto> searchTalksByTitle(String title, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        if (StringUtils.isBlank(title)) {
            // 제목이 공백이면 빈 페이지 반환
            return Page.empty(pageable);
        }

        Page<Talk> talkPage = talkRepository.findByTitleContaining(title, pageable);
        return TalkDto.toDtoPage(talkPage);
    }

}
