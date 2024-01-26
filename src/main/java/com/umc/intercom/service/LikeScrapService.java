package com.umc.intercom.service;

import com.umc.intercom.domain.LikeScrap;
import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.LikeScrapType;
import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.LikeScrapDto;
import com.umc.intercom.repository.LikeScrapRepository;
import com.umc.intercom.repository.TalkRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeScrapService {

    private final LikeScrapRepository likeScrapRepository;
    private final TalkRepository talkRepository;
    private final UserRepository userRepository;

    // 공감 여부 확인
    public boolean checkIfUserLiked(User user, Talk talk) {
        Optional<LikeScrap> like = likeScrapRepository.findByUserAndTalkAndPostType(user, talk, PostType.TALK);
        return like.isPresent();
    }

    public LikeScrapDto addLike(Long talkId, String userEmail) throws Exception {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Talk talk = talkRepository.findById(talkId).orElseThrow(() -> new NotFoundException("Talk 게시글을 찾을 수 없습니다."));

        // 공감 여부 확인
        if (checkIfUserLiked(user, talk)) {
            throw new Exception("이미 공감한 톡톡 입니다.");
        }

        LikeScrap likeScrap = LikeScrap.builder()
                .likeScrapType(LikeScrapType.LIKE)  // 좋아요
                .user(user)
                .talk(talk)
                .postType(PostType.TALK) // 톡톡
                .build();

        likeScrapRepository.save(likeScrap);

        return LikeScrapDto.toDtoFromTalk(likeScrap);
    }

    public void deleteLike(Long talkId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Talk talk = talkRepository.findById(talkId).orElseThrow(() -> new NotFoundException("Talk 게시글을 찾을 수 없습니다."));

        // 공감 여부 확인
        Optional<LikeScrap> like = likeScrapRepository.findByUserAndTalkAndPostType(user, talk, PostType.TALK);

        if (like.isPresent()) {
            // 존재하는 경우 삭제
            likeScrapRepository.delete(like.get());
        } else {
            throw new NotFoundException("공감 내역을 찾을 수 없습니다.");
        }
    }


    // 스크랩

}
