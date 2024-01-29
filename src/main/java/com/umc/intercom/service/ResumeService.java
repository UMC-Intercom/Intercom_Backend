package com.umc.intercom.service;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.ResumeDto;
import com.umc.intercom.repository.PostDetailRepository;
import com.umc.intercom.repository.PostRepository;
import com.umc.intercom.repository.PostSpecRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class ResumeService {
    private PostRepository postRepository;
    private PostDetailRepository postDetailRepository;
    private PostSpecRepository postSpecRepository;
    private UserRepository userRepository;

    public ResumeDto createResume(ResumeDto resumeDto, String userEmail){
        Optional<User> user = userRepository.findByEmail(userEmail);

        Post post = Post.builder()
                .company(resumeDto.getCompany())
                .department(resumeDto.getDepartment())
                .year(resumeDto.getYear())
                .semester(resumeDto.getSemester())
                .postType(resumeDto.getPostType())
                .viewCount(resumeDto.getViewCount())
                .user(user.orElseThrow(() -> new RuntimeException("User not Found")))
                .build();

        post.getUser().setNickname(user.get().getNickname());

        PostDetail postDetail = PostDetail.builder()
                .title(resumeDto.getTitle())
                .content(resumeDto.getContent())
                .imageUrl(resumeDto.getImageUrl())
                .build();

        PostSpec postSpec = PostSpec.builder()
                .education(resumeDto.getEducation())
                .major(resumeDto.getMajor())
                .gpa(resumeDto.getGpa())
                .activity(resumeDto.getActivity())
                .certification(resumeDto.getCertification())
                .english(resumeDto.getEnglish())
                .score(resumeDto.getScore())
                .build();

        Post createdPost = postRepository.save(post);
        PostDetail createdPostDetail = postDetailRepository.save(postDetail);
        PostSpec createdPostSpec = postSpecRepository.save(postSpec);

        return ResumeDto.toDto(createdPost, createdPostDetail, createdPostSpec);


    }
}
