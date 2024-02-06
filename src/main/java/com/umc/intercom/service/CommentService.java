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

        // 코인 부여
        checkAndAddCoins(user.get());

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

        // 코인 부여
        checkAndAddCoins(user.get());

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

    private void checkAndAddCoins(User user) {
        long totalInteractions = commentRepository.countByUser(user);

        // 5의 배수일 때 코인 부여
        if (totalInteractions % 5 == 0) {
            user.setCoin(user.getCoin() + 10);
            userRepository.save(user);
        }
    }

    public boolean checkAdoptionStatus(Long talkId) {
        Optional<Comment> adoptedComment = commentRepository.findByTalkIdAndAdoptionStatus(talkId, AdoptionStatus.ADOPTED);
        return adoptedComment.isPresent();  // 채택된 댓글이 존재하면 true
    }

    public CommentDto.CommentResponseDto adoptComment(String userEmail, Long commentId) {
        Optional<User> user = userRepository.findByEmail(userEmail);
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (user.isPresent()) {
            if (user.get().getEmail().equals(comment.getUser().getEmail())) {   // 나 == 댓글 작성자
                System.out.println("나 댓글: " + user.get().getEmail() + " , " + comment.getUser().getEmail());
                throw new IllegalStateException("내가 작성한 답변은 채택할 수 없습니다.");
            }
        }
        if (!user.get().getEmail().equals(comment.getTalk().getUser().getEmail())) {    // 나 != 톡톡 작성자
            throw new IllegalStateException("톡톡 게시글 작성자만 답변을 채택할 수 있습니다.");
        }
        if (comment.getAdoptionStatus() == AdoptionStatus.ADOPTED) {
            throw new IllegalStateException("이미 채택된 답변입니다.");
        }

        comment.setAdoptionStatus(AdoptionStatus.ADOPTED);
        Comment savedComment = commentRepository.save(comment);
        return CommentDto.CommentResponseDto.toDto(savedComment);
    }

}
