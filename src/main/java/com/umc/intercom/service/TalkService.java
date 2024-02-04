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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TalkService {

    private TalkRepository talkRepository;
    private UserRepository userRepository;

    public TalkDto.TalkResponseDto createTalk(TalkDto.TalkRequestDto talkRequestDto, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        Talk talk = Talk.builder()
                .title(talkRequestDto.getTitle())
                .content(talkRequestDto.getContent())
                .category(talkRequestDto.getCategory())
                .imageUrl(talkRequestDto.getImageUrl())
                .viewCount(0) // 초기 조회수는 0으로 설정
                .likeCount(0)
                .user(user.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .build();

        talk.getUser().setNickname(user.get().getNickname());

        Talk createdTalk = talkRepository.save(talk);
        return TalkDto.TalkResponseDto.toDto(createdTalk);
    }

    public Page<TalkDto.TalkResponseDto> getAllTalks(int page) {
        // 페이징 처리
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        // 페이지 배열은 0부터 시작 -> page-1,  페이지 당 데이터 개수 10개(추후 수정)
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        // jpa에서 기본적으로 제공 -> repo에 적어주지 않아도o
        Page<Talk> talkPage =  talkRepository.findAll(pageable);
        return TalkDto.toDtoPage(talkPage);
    }

    public Page<TalkDto.TalkResponseDto> getAllTalksByViewCounts(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("viewCount"));
        sorts.add(Sort.Order.desc("createdAt"));    // 조회수가 동일하면 최신순으로 정렬
        
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Talk> talkPage = talkRepository.findAll(pageable);
        return TalkDto.toDtoPage(talkPage);
    }

    public Page<TalkDto.TalkResponseDto> getAllTalksByLikeCounts(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("likeCount"));
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Talk> talkPage = talkRepository.findAll(pageable);
        return TalkDto.toDtoPage(talkPage);
    }

    public Optional<TalkDto.TalkResponseDto> getTalkById(String userEmail, Long id) {
        Optional<Talk> optionalTalk = talkRepository.findById(id);

        if (optionalTalk.isPresent()) {
            Talk talk = optionalTalk.get();

            if (!talk.getUser().getEmail().equals(userEmail)) {
                // 작성자 != 현재 로그인 한 유저
                talk.setViewCount(talk.getViewCount()+1);
                talkRepository.save(talk);
            }
        }

        return optionalTalk.map(TalkDto.TalkResponseDto::toDto);
    }

    public Page<TalkDto.TalkResponseDto> searchTalksByTitle(String title, int page) {
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
