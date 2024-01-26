package com.umc.intercom.service;

import com.umc.intercom.domain.Comment;
import com.umc.intercom.domain.User;
import com.umc.intercom.dto.CommentDto;
import com.umc.intercom.repository.CommentRepository;
import com.umc.intercom.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;


    @Transactional
    public CommentDto createComment(CommentDto commentDto, String userEmail) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .adoptionStatus(commentDto.getAdoptionStatus())
                .user(user.orElseThrow(() -> new RuntimeException("User not found")))
                .build();

        comment.getUser().setNickname(user.get().getNickname());

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.toDto(savedComment);
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));
        comment.updateContent(commentDto.getContent());
        return CommentDto.toDto(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id=" + id));
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto> getComments(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(CommentDto::toDto).collect(Collectors.toList());
    }
}
