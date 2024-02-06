package com.umc.intercom.service;

import com.umc.intercom.aws.AmazonS3Manager;
import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.Uuid;
import com.umc.intercom.dto.TalkDto;
import com.umc.intercom.repository.TalkRepository;
import com.umc.intercom.repository.UserRepository;
import com.umc.intercom.repository.UuidRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class TalkService {

    private final TalkRepository talkRepository;
    private final UserRepository userRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;

    @Transactional
    public TalkDto.TalkResponseDto createTalk(TalkDto.TalkRequestDto talkRequestDto, List<MultipartFile> files, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        // 이미지 업로드
        List<String> pictureUrls = new ArrayList<>(); // 이미지 URL들을 저장할 리스트
        if (files != null && !files.isEmpty()){
            for (MultipartFile file : files) {
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder()
                        .uuid(uuid).build());
                String pictureUrl = s3Manager.uploadFile(s3Manager.generateTalkKeyName(savedUuid), file);
                pictureUrls.add(pictureUrl); // 리스트에 이미지 URL 추가

                System.out.println("s3 url(클릭 시 브라우저에 사진 뜨는지 확인): " + pictureUrl);
            }
        }

        Talk talk = Talk.builder()
                .title(talkRequestDto.getTitle())
                .content(talkRequestDto.getContent())
                .category(talkRequestDto.getCategory())
                .imageUrls(pictureUrls)  // 이미지 URL을 S3 업로드 후의 URL로 설정
                .viewCount(0)
                .likeCount(0)
                .user(user.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")))
                .build();

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

    public Page<TalkDto.TalkResponseDto> getTalksWithCommentCounts(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Order.desc("createdAt")));

        Page<Object[]> result = talkRepository.findTalksWithCommentCounts(pageable);

        List<TalkDto.TalkResponseDto> talkDtoList = new ArrayList<>();
        for (Object[] row : result.getContent()) {
            Talk talk = (Talk) row[0];
            int commentCount = ((Number) row[1]).intValue(); // Long에서 int로 변환
            TalkDto.TalkResponseDto talkDto = TalkDto.TalkResponseDto.toDto(talk);
            talkDto.setCommentCount(commentCount);
            talkDtoList.add(talkDto);
        }

        return new PageImpl<>(talkDtoList, pageable, result.getTotalElements());
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
