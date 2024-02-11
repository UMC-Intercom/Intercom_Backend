package com.umc.intercom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.umc.intercom.aws.AmazonS3Manager;
import com.umc.intercom.domain.*;
import com.umc.intercom.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.InterviewDto;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class InterviewService {
    
    private PostRepository postRepository;
    private PostDetailRepository postDetailRepository;
    private PostSpecRepository postSpecRepository;
    private UserRepository userRepository;
    private UuidRepository uuidRepository;
    private AmazonS3Manager s3Manager;


    @Transactional
    public InterviewDto.InterviewResponseDto createInterview(List<MultipartFile> files, InterviewDto.InterviewRequestDto  interviewDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);

        // 이미지 업로드
        List<String> pictureUrls = new ArrayList<>(); // 이미지 URL들을 저장할 리스트
        if (files != null && !files.isEmpty()){
            for (MultipartFile file : files) {
                String uuid = UUID.randomUUID().toString();
                Uuid savedUuid = uuidRepository.save(Uuid.builder()
                        .uuid(uuid).build());
                String pictureUrl = s3Manager.uploadFile(s3Manager.generatePostKeyName(savedUuid), file);
                pictureUrls.add(pictureUrl); // 리스트에 이미지 URL 추가

                System.out.println("s3 url(클릭 시 브라우저에 사진 뜨는지 확인): " + pictureUrl);
            }
        }

        Post post = Post.builder()
                        .company(interviewDto.getCompany())
                        .department(interviewDto.getDepartment())
                        .year(interviewDto.getYear())
                        .semester(interviewDto.getSemester())
                        .postType(PostType.INTERVIEW_REVIEW)    // 저장할 때 postType 타입 지정
                        .viewCount(0)
                        .user(user.orElseThrow(() -> new RuntimeException("User not Found")))
                        .build();
//        post.getUser().setNickname(user.get().getNickname());
        
        Post createdPost = postRepository.save(post);
        
        
        PostDetail postDetail = PostDetail.builder()
                                        .title(interviewDto.getTitle())
                                        .content(interviewDto.getContent())
                                        .imageUrls(pictureUrls)   // S3 url 저장
                                        .post(createdPost)
                                        .build();

        PostSpec postSpec = PostSpec.builder()
                                    .education(interviewDto.getEducation())
                                    .major(interviewDto.getMajor())
                                    .gpa(interviewDto.getGpa())
                                    .activity(interviewDto.getActivity())
                                    .certification(interviewDto.getCertification())
                                    .english(interviewDto.getEnglish())
                                    .score(interviewDto.getScore())
                                    .post(createdPost)
                                    .build();

        PostDetail createdPostDetail = postDetailRepository.save(postDetail);
        PostSpec createdPostSpec = postSpecRepository.save(postSpec);

        return InterviewDto.InterviewResponseDto.toDto(createdPost, createdPostDetail, createdPostSpec);
    }
    
    
    public List<InterviewDto.InterviewResponseDto> getAllInterviews() {
        List<Post> posts = postRepository.findByPostTypeOrderByCreatedAtDesc(PostType.INTERVIEW_REVIEW);
        
        return posts.stream().map(post -> {
            PostDetail postDetail = postDetailRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostDetail not Found"));
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetail, postSpec);
        }).collect(Collectors.toList());
    }

    public Page<InterviewDto.InterviewResponseDto> getAllInterviewsByScrapCounts(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("scrapCount"));
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Post> posts = postRepository.findByPostType(PostType.INTERVIEW_REVIEW, pageable);

        return posts.map(post -> {
            PostDetail postDetail = postDetailRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostDetail not Found"));
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetail, postSpec);
        });
    }

    public Page<InterviewDto.InterviewResponseDto> getMyInterviews(String userEmail, int page) {
        Optional<User> user = userRepository.findByEmail(userEmail);

        if (!user.isPresent()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("scrapCount"));
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Post> posts = postRepository.findByUserAndPostType(user.get(), PostType.INTERVIEW_REVIEW, pageable);

        return posts.map(post -> {
            PostDetail postDetail = postDetailRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostDetail not Found"));
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetail, postSpec);
        });

    }
}
