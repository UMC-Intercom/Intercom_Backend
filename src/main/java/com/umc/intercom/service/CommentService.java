package com.umc.intercom.service;

import com.umc.intercom.domain.Comment;
import com.umc.intercom.domain.Notification;
import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.AdoptionStatus;
import com.umc.intercom.dto.CommentDto;
import com.umc.intercom.repository.CommentRepository;
import com.umc.intercom.repository.NotificationRepository;
import com.umc.intercom.repository.TalkRepository;
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
    private TalkRepository talkRepository;
    private NotificationRepository notificationRepository;

    @Transactional
    public CommentDto.CommentResponseDto  createComment(String userEmail, CommentDto.CommentRequestDto commentDto) {
        Optional<Talk> talk = talkRepository.findById(commentDto.getTalkId());
        Optional<User> user = userRepository.findByEmail(userEmail);
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .adoptionStatus(AdoptionStatus.NOT_ADOPTED)     // 초기 상태는 채택x로 설정
                .talk(talk.orElseThrow(() -> new RuntimeException("Talk not found")))
                .user(user.orElseThrow(() -> new RuntimeException("User not found")))
                .build();

        Comment savedComment = commentRepository.save(comment);

        // 알림 전송
        sendNotification(savedComment.getUser(), savedComment);

        return CommentDto.CommentResponseDto.toDto(savedComment);
    }

    @Transactional
    public CommentDto.CommentResponseDto updateComment(Long id, CommentDto.CommentRequestDto commentDto) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id= " + id));
        comment.updateContent(commentDto.getContent());
        return CommentDto.CommentResponseDto.toDto(comment);
    }

    @Transactional
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다. id= " + id));
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentDto.CommentResponseDto> getComments(Long talkId) {
        List<Comment> comments = commentRepository.findAllByTalkId(talkId); // talk은 finaAllByTalkId, post는 findAllByPostId로
        return comments.stream().map(CommentDto.CommentResponseDto::toDto).collect(Collectors.toList());
    }

    @Transactional
    public CommentDto.CommentResponseDto createReplyComment(String userEmail, CommentDto.ReplyRequestDto commentDto) {
        Optional<Talk> talk = talkRepository.findById(commentDto.getTalkId());
        Optional<User> user = userRepository.findByEmail(userEmail);
        Optional<Comment> parentId = commentRepository.findById(commentDto.getParentId());
        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .adoptionStatus(AdoptionStatus.NOT_ADOPTED)     // 초기 상태는 채택x로 설정
                .talk(talk.orElseThrow(() -> new RuntimeException("Talk not found")))
                .user(user.orElseThrow(() -> new RuntimeException("User not found")))
                .parentId(parentId.orElseThrow(() -> new RuntimeException("Parent comment not found")))
                .build();

        Comment savedComment = commentRepository.save(comment);
        // 알림 전송
        sendNotification(savedComment.getUser(), savedComment);

        return CommentDto.CommentResponseDto.toDto(savedComment);
    }

    private void sendNotification(User user, Comment comment) {
        Notification notification = Notification.builder()
                .user(user)
                .comment(comment)
                .isRead(false)  // 초기에 알림은 읽지 않음
                .build();

        notificationRepository.save(notification);
    }
}
