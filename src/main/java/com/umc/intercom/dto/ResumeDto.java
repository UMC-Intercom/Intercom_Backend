package com.umc.intercom.dto;

import com.umc.intercom.domain.Post;
import com.umc.intercom.domain.PostDetail;
import com.umc.intercom.domain.PostSpec;
import com.umc.intercom.domain.common.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResumeDto {
    //Post 필드
    private Long id;
    private String company;
    private String department;
    private String year; //합격 년도
    private String semester; //상반기 , 하반기
    private PostType postType;
    private int viewCount;
    private String writer;
    //PostDetail 필드
    private String title;
    private String content;
    private String imageUrl;
    //PostSpec 필드
    private String education;
    private String major;
    private String gpa;
    private String activity;
    private String certification;
    private String english;
    private String score;

    public static ResumeDto toDto(Post post, PostDetail postDetail, PostSpec postSpec){
        return new ResumeDto(
                post.getId(),
                post.getCompany(),
                post.getDepartment(),
                post.getYear(),
                post.getSemester(),
                post.getPostType(),
                post.getViewCount(),
                post.getUser().getNickname(),
                postDetail.getTitle(),
                postDetail.getContent(),
                postDetail.getImageUrl(),
                postSpec.getEducation(),
                postSpec.getMajor(),
                postSpec.getGpa(),
                postSpec.getActivity(),
                postSpec.getCertification(),
                postSpec.getEnglish(),
                postSpec.getScore()
        );
    }

    public static ResumeDto toScrapListDto(Post post) {
        ResumeDto dto = new ResumeDto();

        dto.setId(post.getId());
        dto.setCompany(post.getCompany());
        dto.setDepartment(post.getDepartment());
        dto.setYear(post.getYear());
        dto.setSemester(post.getSemester());
        dto.setPostType(post.getPostType());
        dto.setViewCount(post.getViewCount());
        dto.setWriter(post.getUser().getNickname());

        return dto;
    }

}
