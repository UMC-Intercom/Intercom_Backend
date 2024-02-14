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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final PostRepository postRepository;
    private final PostDetailRepository postDetailRepository;
    private final PostSpecRepository postSpecRepository;


    public Page<ResumeDto.ResumeResponseDto> searchResumesByDepartment(String department, int page) {
        Pageable pageable = PageRequest.of(page - 1, 4, Sort.by("id").descending());
        Page<Post> posts = postRepository.findByPostTypeAndDepartmentContaining(PostType.SUCCESSFUL_RESUME, department, pageable);

        return posts.map(post -> {
            List<PostDetail> postDetails = postDetailRepository.findAllByPost(post);
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return ResumeDto.ResumeResponseDto.toDto(post, postDetails, postSpec);
        });
    }

    public Page<InterviewDto.InterviewResponseDto> searchInterviewsByDepartment(String department, int page) {
        Pageable pageable = PageRequest.of(page - 1, 4, Sort.by("id").descending());
        Page<Post> posts = postRepository.findByPostTypeAndDepartmentContaining(PostType.INTERVIEW_REVIEW, department, pageable);

        return posts.map(post -> {
            List<PostDetail> postDetails = postDetailRepository.findAllByPost(post);
            PostSpec postSpec = postSpecRepository.findByPost(post).orElseThrow(() -> new RuntimeException("PostSpec not Found"));
            return InterviewDto.InterviewResponseDto.toDto(post, postDetails.get(0), postSpec);
        });
    }
}