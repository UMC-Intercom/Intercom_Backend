package com.umc.intercom.service;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.InterviewDto;
import com.umc.intercom.dto.ResumeDto;
import com.umc.intercom.repository.PostDetailRepository;
import com.umc.intercom.repository.PostRepository;
import com.umc.intercom.repository.PostSpecRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final PostSpecRepository postSpecRepository;


    public List<Object> searchResumesByDepartment(String department){
        List<Post> posts = postRepository.findByDepartmentContaining(department);

        return posts.stream().map(post -> {
            List<PostDetail> postDetails = postDetailRepository.findAllByPost(post);
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));

            if (post.getPostType() == PostType.SUCCESSFUL_RESUME) {
                return ResumeDto.ResumeResponseDto.toDto(post, postDetails, postSpec);
            } else {
                return null;
            }
        }).collect(Collectors.toList());
    }

    public List<Object> searchInterviewsByDepartment(String department){
        List<Post> posts = postRepository.findByDepartmentContaining(department);

        return posts.stream().map(post -> {
            List<PostDetail> postDetails = postDetailRepository.findAllByPost(post);
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));

             if (post.getPostType() == PostType.INTERVIEW_REVIEW) {
                return InterviewDto.InterviewResponseDto.toDto(post, postDetails.get(0), postSpec);
            } else {
                return null;
            }
        }).collect(Collectors.toList());
    }
}
