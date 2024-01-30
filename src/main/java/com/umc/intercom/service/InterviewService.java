package com.umc.intercom.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.InterviewDto;
import com.umc.intercom.repository.PostDetailRepository;
import com.umc.intercom.repository.PostRepository;
import com.umc.intercom.repository.PostSpecRepository;
import com.umc.intercom.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InterviewService {
    
    private PostRepository postRepository;
    private PostDetailRepository postDetailRepository;
    private PostSpecRepository postSpecRepository;
    private UserRepository userRepository;
    
    public InterviewDto createInterview(InterviewDto interviewDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);
    
        
        Post post = Post.builder()
                        .company(interviewDto.getCompany())
                        .department(interviewDto.getDepartment())
                        .year(interviewDto.getYear())
                        .semester(interviewDto.getSemester())
                        .postType(interviewDto.getPostType())
                        .viewCount(interviewDto.getViewCount())
                        .user(user.orElseThrow(() -> new RuntimeException("User not Found")))
                        .build();
        post.getUser().setNickname(user.get().getNickname());
        
        
        PostDetail postDetail = PostDetail.builder()
                                        .title(interviewDto.getTitle())
                                        .content(interviewDto.getContent())
                                        .imageUrl(interviewDto.getImageUrl())
                                        .build();

        PostSpec postSpec = PostSpec.builder()
                                    .education(interviewDto.getEducation())
                                    .major(interviewDto.getMajor())
                                    .gpa(interviewDto.getGpa())
                                    .activity(interviewDto.getActivity())
                                    .certification(interviewDto.getCertification())
                                    .english(interviewDto.getEnglish())
                                    .score(interviewDto.getScore())
                                    .build();

        Post createdPost = postRepository.save(post);
        PostDetail createdPostDetail = postDetailRepository.save(postDetail);
        PostSpec createdPostSpec = postSpecRepository.save(postSpec);

        return InterviewDto.toDto(createdPost, createdPostDetail, createdPostSpec);
    }
}
