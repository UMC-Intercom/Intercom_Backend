package com.umc.intercom.service;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.ResumeDto;
import com.umc.intercom.repository.PostDetailRepository;
import com.umc.intercom.repository.PostRepository;
import com.umc.intercom.repository.PostSpecRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
@Service
public class ResumeService {
    private PostRepository postRepository;
    private PostDetailRepository postDetailRepository;
    private PostSpecRepository postSpecRepository;
    private UserRepository userRepository;

    @Transactional
    public ResumeDto.ResumeResponseDto  createResume(ResumeDto.ResumeRequestDto  resumeDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);

        Post post = Post.builder()
                .company(resumeDto.getCompany())
                .department(resumeDto.getDepartment())
                .year(resumeDto.getYear())
                .semester(resumeDto.getSemester())
                .postType(PostType.SUCCESSFUL_RESUME)   // 저장할 때 postType 타입 지정
                .viewCount(0)
                .user(user.orElseThrow(() -> new RuntimeException("User not Found")))
                .build();

//        post.getUser().setNickname(user.get().getNickname());

        List<PostDetail> postDetails = IntStream.range(0, resumeDto.getTitles().size())
                .mapToObj(i -> PostDetail.builder()
                        .post(post)
                        .title(resumeDto.getTitles().get(i))
                        .content(resumeDto.getContents().get(i))
                        .build())
                .collect(Collectors.toList());

//        postDetail.getPost().setId(resumeDto.getId());

        String certifications = String.join(", ", resumeDto.getCertifications());
        PostSpec postSpec = PostSpec.builder()
                .post(post)
                .education(resumeDto.getEducation())
                .major(resumeDto.getMajor())
                .gpa(resumeDto.getGpa())
                .activity(resumeDto.getActivity())
                .certification(certifications)
                .english(resumeDto.getEnglish())
                .score(resumeDto.getScore())
                .build();

//        postSpec.getPost().setId(resumeDto.getId());

        Post createdPost = postRepository.save(post);
        List<PostDetail> createdPostDetails = postDetailRepository.saveAll(postDetails);
        PostSpec createdPostSpec = postSpecRepository.save(postSpec);

        return ResumeDto.ResumeResponseDto.toDto(createdPost, createdPostDetails, createdPostSpec);
    }

    public Page<ResumeDto.ResumeResponseDto> getAllResumes(int page){
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createdAt"));

        Pageable pageable = PageRequest.of(page-1, 10, Sort.by(sorts));

        //POSTTYPE으로 SUCCESSFUL_RESUME만 조회
        Page<Post> postPage = postRepository.findByPostType(PostType.SUCCESSFUL_RESUME, pageable);

        // Post의 id 리스트를 만듬
        List<Long> postIds = postPage.getContent().stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // id 리스트로 PostDetail, PostSpec 찾기
        List<PostDetail> postDetails = postDetailRepository.findByPostIdIn(postIds);
        List<PostSpec> postSpecs = postSpecRepository.findByPostIdIn(postIds);

        // PostDetail, PostSpec을 Post의 id를 key로 하는 Map으로 변환
        Map<Long, List<PostDetail>> postDetailMap = postDetails.stream()
                .collect(Collectors.groupingBy(detail -> detail.getPost().getId()));
        Map<Long, PostSpec> postSpecMap = postSpecs.stream()
                .collect(Collectors.toMap(spec -> spec.getPost().getId(), Function.identity()));

        // Post, PostDetail, PostSpec을 ResumeDto로 변환
        List<ResumeDto.ResumeResponseDto> resumeDtos = postPage.getContent().stream()
                .map(post -> ResumeDto.ResumeResponseDto.toDto(post, postDetailMap.get(post.getId()), postSpecMap.get(post.getId())))
                .collect(Collectors.toList());

        // 변환된 결과를 Page로
        return new PageImpl<>(resumeDtos, pageable, postPage.getTotalElements());
    }

    public Optional<ResumeDto.ResumeResponseDto> getResumeById(Long id){
        Optional<Post> post = postRepository.findById(id);

        if(post.isPresent()){
            if(post.get().getPostType() != PostType.SUCCESSFUL_RESUME)
                return Optional.empty();
            else {
                List<PostDetail> postDetails = postDetailRepository.findAllByPost(post.get());
                PostSpec postSpec = postSpecRepository.findByPost(post.get()).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
                return Optional.of(ResumeDto.ResumeResponseDto.toDto(post.get(), postDetails, postSpec));
            }
        }
        return Optional.empty();
    }

}
