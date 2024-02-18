package com.umc.intercom.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.umc.intercom.aws.AmazonS3Manager;
import com.umc.intercom.domain.*;
import com.umc.intercom.domain.common.enums.Gender;
import com.umc.intercom.repository.*;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.InterviewDto;
import com.umc.intercom.dto.ResumeDto;

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

    @Transactional
    public InterviewDto.InterviewResponseDto createInterview(InterviewDto.InterviewRequestDto  interviewDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);

        // 성별
        Gender gender;
        if (interviewDto.getGender().equals("male")) {
            gender = Gender.MALE;
        }
        else if (interviewDto.getGender().equals("female")) {
            gender = Gender.FEMALE;
        }
        else {  // (requestDto.getGender().equals("no-selected")
            gender = Gender.NONE;
        }

        Post post = Post.builder()
                        .company(interviewDto.getCompany())
                        .department(interviewDto.getDepartment())
                        .year(interviewDto.getYear())
                        .semester(interviewDto.getSemester())
                        .gender(gender)
                        .birthday(interviewDto.getBirthday())
                        .postType(PostType.INTERVIEW_REVIEW)    // 저장할 때 postType 타입 지정
                        .viewCount(0)
                        .user(user.orElseThrow(() -> new RuntimeException("User not Found")))
                        .build();

        PostDetail postDetails = PostDetail.builder()
                .post(post)
                .content(interviewDto.getContents())
                .build();

        String certifications = String.join(", ", interviewDto.getCertifications());
        PostSpec postSpec = PostSpec.builder()
                .post(post)
                .education(interviewDto.getEducation())
                .major(interviewDto.getMajor())
                .gpa(interviewDto.getGpa())
                .activity(interviewDto.getActivity())
                .certification(certifications)
                .english(interviewDto.getEnglish())
                .score(interviewDto.getScore())
                .build();


        Post createdPost = postRepository.save(post);
        PostDetail createdPostDetails = postDetailRepository.save(postDetails);
        PostSpec createdPostSpec = postSpecRepository.save(postSpec);

        // 코인 부여
        user.get().setCoin(user.get().getCoin() + 30);
        userRepository.save(user.get());

        return InterviewDto.InterviewResponseDto.toDto(createdPost, createdPostDetails, createdPostSpec);
    }
    
    // 최신순으로 조회
    public Page<InterviewDto.InterviewResponseDto> getAllInterviews(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));
        
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));
        
        Page<Post> interviewPage = postRepository.findByPostType(PostType.INTERVIEW_REVIEW, pageable);

        return interviewPage.map(post -> {
            PostDetail postDetail = postDetailRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostDetail not Found"));
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetail, postSpec);
        });
    }
    
    // 기업명, 직무명으로 면접 후기 검색
    public Page<InterviewDto.InterviewResponseDto> getAllInterviewsByCompanyAndDepartment(String company, String department, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Post> interviewPage;
        if (company != null && department != null) {
            interviewPage = postRepository.findByCompanyContainingAndDepartmentContainingAndPostType(company, department, PostType.INTERVIEW_REVIEW, pageable);
        } else if (company == null && department != null) {
            interviewPage = postRepository.findByDepartmentContainingAndPostType(department, PostType.INTERVIEW_REVIEW, pageable);
        } else if (company != null && department == null) {
            interviewPage = postRepository.findByCompanyContainingAndPostType(company, PostType.INTERVIEW_REVIEW, pageable);
        } else {
            interviewPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return interviewPage.map(post -> {
            PostDetail postDetail = postDetailRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostDetail not Found"));
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetail, postSpec);
        });
    }

    public Page<InterviewDto.InterviewResponseDto> getAllInterviewsByCompanyAndDepartmentByScrapCounts(String company, String department, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("scrapCount"));
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        Page<Post> interviewPage;
        if (company != null && department != null) {
            interviewPage = postRepository.findByCompanyContainingAndDepartmentContainingAndPostType(company, department, PostType.INTERVIEW_REVIEW, pageable);
        } else if (company == null && department != null) {
            interviewPage = postRepository.findByDepartmentContainingAndPostType(department, PostType.INTERVIEW_REVIEW, pageable);
        } else if (company != null && department == null) {
            interviewPage = postRepository.findByCompanyContainingAndPostType(company, PostType.INTERVIEW_REVIEW, pageable);
        } else {
            interviewPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return interviewPage.map(post -> {
            PostDetail postDetail = postDetailRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostDetail not Found"));
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetail, postSpec);
        });
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

        Pageable pageable = PageRequest.of(page-1, 4, Sort.by(sorts));

        Page<Post> posts = postRepository.findByUserAndPostType(user.get(), PostType.INTERVIEW_REVIEW, pageable);

        return posts.map(post -> {
            PostDetail postDetail = postDetailRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostDetail not Found"));
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetail, postSpec);
        });
    }

    public Optional<InterviewDto.InterviewResponseDto> getInterviewById(Long id){
        Optional<Post> post = postRepository.findById(id);

        if(post.isPresent()){
            if(post.get().getPostType() != PostType.INTERVIEW_REVIEW)
                return Optional.empty();
        else {
            List<PostDetail> postDetails = postDetailRepository.findAllByPost(post.get());
            PostSpec postSpec = postSpecRepository.findByPost(post.get()).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return Optional.of(InterviewDto.InterviewResponseDto.toDto(post.get(), postDetails.get(0), postSpec));
        }
    }
        return Optional.empty();
    }
}
