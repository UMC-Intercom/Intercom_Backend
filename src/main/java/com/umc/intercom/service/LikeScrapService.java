package com.umc.intercom.service;

import com.umc.intercom.domain.*;
import com.umc.intercom.domain.common.enums.LikeScrapType;
import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.*;
import com.umc.intercom.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class LikeScrapService {

    private final LikeScrapRepository likeScrapRepository;
    private final TalkRepository talkRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final NotificationRepository notificationRepository;
    private final JobRepository jobRepository;

    /* talk 좋아요 */
    public Optional<LikeScrap> checkIfUserLiked(User user, Talk talk) {
        return likeScrapRepository.findByUserAndTalkAndPostTypeAndLikeScrapType(user, talk, PostType.TALK, LikeScrapType.LIKE);
    }

    @Transactional
    public LikeScrapDto addLike(Long talkId, String userEmail) throws Exception {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Talk talk = talkRepository.findById(talkId).orElseThrow(() -> new NotFoundException("Talk 게시글을 찾을 수 없습니다."));

        // 공감 여부 확인
        Optional<LikeScrap> likeOptional = checkIfUserLiked(user, talk);
        if (likeOptional.isPresent()) {
            throw new Exception("이미 공감한 톡톡 입니다.");
        }

        LikeScrap like = LikeScrap.builder()
                .likeScrapType(LikeScrapType.LIKE)  // 좋아요
                .user(user)
                .talk(talk)
                .postType(PostType.TALK) // 톡톡
                .build();

        likeScrapRepository.save(like);

        // 좋아요 수 업데이트
        talk.setLikeCount(talk.getLikeCount() + 1);
        talkRepository.save(talk);

        // 코인 부여
        checkAndAddCoins(user);

        // 알림 전송
        sendNotification(like.getUser(), like);

        return LikeScrapDto.toDtoFromTalk(like);
    }

    private void checkAndAddCoins(User user) {
        long totalInteractions = likeScrapRepository.countByUserAndLikeScrapType(user, LikeScrapType.LIKE);

        // 5의 배수일 때 코인 부여
        if (totalInteractions % 5 == 0) {
            user.setCoin(user.getCoin() + 10);
            userRepository.save(user);
        }
    }

    @Transactional
    public void deleteLike(Long talkId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Talk talk = talkRepository.findById(talkId).orElseThrow(() -> new NotFoundException("Talk 게시글을 찾을 수 없습니다."));

        // 공감 여부 확인
        Optional<LikeScrap> likeOptional = checkIfUserLiked(user, talk);
        if (likeOptional.isPresent()) {
            likeScrapRepository.delete(likeOptional.get());

            // 좋아요 수 업데이트
            talk.setLikeCount(talk.getLikeCount() - 1);
            talkRepository.save(talk);
        }
    }


    /* talk 스크랩 */
    public Optional<LikeScrap> checkIfUserScrapedTalk(User user, Talk talk) {
        return likeScrapRepository.findByUserAndTalkAndPostTypeAndLikeScrapType(user, talk, PostType.TALK, LikeScrapType.SCRAP);
    }

    @Transactional
    public LikeScrapDto addTalkScrap(Long talkId, String userEmail) throws Exception {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Talk talk = talkRepository.findById(talkId).orElseThrow(() -> new NotFoundException("Talk 게시글을 찾을 수 없습니다."));

        // 스크랩 여부 확인
        Optional<LikeScrap> scrapOptional = checkIfUserScrapedTalk(user, talk);
        if (scrapOptional.isPresent()) {
            throw new Exception("이미 스크랩한 톡톡 입니다.");
        }

        LikeScrap scrap = LikeScrap.builder()
                .likeScrapType(LikeScrapType.SCRAP)  // 스크랩
                .user(user)
                .talk(talk)
                .postType(PostType.TALK) // 톡톡
                .build();

        likeScrapRepository.save(scrap);

        // 스크랩 수 업데이트
        talk.setScrapCount(talk.getScrapCount() + 1);
        talkRepository.save(talk);

        return LikeScrapDto.toDtoFromTalk(scrap);
    }

    @Transactional
    public void deleteTalkScrap(Long talkId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Talk talk = talkRepository.findById(talkId).orElseThrow(() -> new NotFoundException("Talk 게시글을 찾을 수 없습니다."));

        // 스크랩 여부 확인
        Optional<LikeScrap> scrapOptional = checkIfUserScrapedTalk(user, talk);
        if (scrapOptional.isPresent()) {
            likeScrapRepository.delete(scrapOptional.get());

            // 스크랩 수 업데이트
            talk.setScrapCount(talk.getScrapCount() - 1);
            talkRepository.save(talk);

        }
    }


    /* post 스크랩 */
    public Optional<LikeScrap> checkIfUserScrapedPost(User user, Post post) {
        return likeScrapRepository.findByUserAndPostAndPostTypeAndLikeScrapType(user, post, post.getPostType(), LikeScrapType.SCRAP);
    }

    @Transactional
    public LikeScrapDto addPostScrap(Long postId, String userEmail) throws Exception {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post 게시글을 찾을 수 없습니다."));

        // 스크랩 여부 확인
        Optional<LikeScrap> scrapOptional = checkIfUserScrapedPost(user, post);
        if (scrapOptional.isPresent()) {
            throw new Exception("이미 스크랩한 post 입니다.");
        }

        LikeScrap scrap = LikeScrap.builder()
                .likeScrapType(LikeScrapType.SCRAP)  // 스크랩
                .user(user)
                .post(post)
                .postType(post.getPostType()) // 공고, 면접후기, 합격자소서
                .build();

        likeScrapRepository.save(scrap);

        // 스크랩 수 업데이트
        post.setScrapCount(post.getScrapCount() + 1);
        postRepository.save(post);

        return LikeScrapDto.toDtoFromPost(scrap);
    }

    @Transactional
    public void deletePostScrap(Long postId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Post 게시글을 찾을 수 없습니다."));

        // 스크랩 여부 확인
        Optional<LikeScrap> scrapOptional = checkIfUserScrapedPost(user, post);
        if (scrapOptional.isPresent()) {
            likeScrapRepository.delete(scrapOptional.get());

            // 스크랩 수 업데이트
            post.setScrapCount(post.getScrapCount() - 1);
            postRepository.save(post);

        }
    }

    public Page<TalkDto.TalkResponseDto> getAllTalkScraps(String userEmail, int page) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 페이징
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));   // 최근에 스크랩한 순서대로
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<LikeScrap> scrapPage = likeScrapRepository.findByUserAndLikeScrapTypeAndPostType(user, LikeScrapType.SCRAP, PostType.TALK, pageable);

        // LikeScrap을 TalkDto로 변환해서 반환
        return scrapPage.map(scrap -> TalkDto.TalkResponseDto.toDto(scrap.getTalk()));
    }

    public Page<InterviewDto.ScrapResponseDto> getAllInterviewScraps(String userEmail, int page) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<LikeScrap> scrapPage = likeScrapRepository.findByUserAndLikeScrapTypeAndPostType(user, LikeScrapType.SCRAP, PostType.INTERVIEW_REVIEW, pageable);

        // LikeScrap을 InterviewDto로 변환해서 반환
        return scrapPage.map(scrap -> InterviewDto.toScrapListDto(scrap.getPost()));
    }

    public Page<ResumeDto.ScrapResponseDto> getAllResumeScraps(String userEmail, int page) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<LikeScrap> scrapPage = likeScrapRepository.findByUserAndLikeScrapTypeAndPostType(user, LikeScrapType.SCRAP, PostType.SUCCESSFUL_RESUME, pageable);

        // LikeScrap을 ResumeDto로 변환해서 반환
        return scrapPage.map(scrap -> ResumeDto.toScrapListDto(scrap.getPost()));
    }

    private void sendNotification(User user, LikeScrap likeScrap) {
        Notification notification = Notification.builder()
                .user(user)
                .likeScrap(likeScrap)
                .isRead(false)  // 초기에 알림은 읽지 않음
                .build();

        notificationRepository.save(notification);
    }

    /* 공고 스크랩 */
    public Optional<LikeScrap> checkIfUserScrapedJob(User user, Job job) {
        return likeScrapRepository.findByUserAndJobAndPostTypeAndLikeScrapType(user, job, PostType.JOB_INFO, LikeScrapType.SCRAP);
    }

    @Transactional
    public LikeScrapDto addJobScrap(Long id, String userEmail) throws Exception {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Job job = jobRepository.findById(id).orElseThrow(() -> new NotFoundException("Job 게시글을 찾을 수 없습니다."));

        // 스크랩 여부 확인
        Optional<LikeScrap> scrapOptional = checkIfUserScrapedJob(user, job);
        if (scrapOptional.isPresent()) {
            throw new Exception("이미 스크랩한 공고 입니다.");
        }

        LikeScrap scrap = LikeScrap.builder()
                .likeScrapType(LikeScrapType.SCRAP)  // 스크랩
                .user(user)
                .job(job)
                .postType(PostType.JOB_INFO) // 공고
                .build();

        likeScrapRepository.save(scrap);

        // 스크랩 수 업데이트
        job.setScrapCount(job.getScrapCount() + 1);
        jobRepository.save(job);

        return LikeScrapDto.toDtoFromJob(scrap);
    }

    @Transactional
    public void deleteJobScrap(Long id, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        Job job = jobRepository.findById(id).orElseThrow(() -> new NotFoundException("Job 게시글을 찾을 수 없습니다."));

        // 스크랩 여부 확인
        Optional<LikeScrap> scrapOptional = checkIfUserScrapedJob(user, job);
        if (scrapOptional.isPresent()) {
            likeScrapRepository.delete(scrapOptional.get());

            // 스크랩 수 업데이트
            job.setScrapCount(job.getScrapCount() - 1);
            jobRepository.save(job);
        }
    }

    public Page<JobDto.JobListResponseDto> getAllJobScraps(String userEmail, int page) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<LikeScrap> scrapPage = likeScrapRepository.findByUserAndLikeScrapTypeAndPostType(user, LikeScrapType.SCRAP, PostType.JOB_INFO, pageable);

        return scrapPage.map(scrap -> JobDto.JobListResponseDto.toScrapListDto(scrap.getJob()));
    }
}
