package com.umc.intercom.service;

import com.umc.intercom.domain.Comment;
import com.umc.intercom.domain.Notification;
import com.umc.intercom.domain.Talk;
import com.umc.intercom.domain.User;
import com.umc.intercom.domain.common.enums.AdoptionStatus;
import com.umc.intercom.domain.common.enums.PostType;
import com.umc.intercom.dto.CommentDto;
import com.umc.intercom.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private LikeScrapRepository likeScrapRepository;

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

        // 댓글 수 업데이트
        talk.get().setCommentCount(talk.get().getCommentCount() + 1);
        talkRepository.save(talk.get());

        // 코인 부여 - 댓글 작성 시 2코인
        user.get().setCoin(user.get().getCoin() + 2);
        userRepository.save(user.get());

        // 알림 전송
        sendNotification(savedComment.getTalk().getUser(), savedComment);

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
    public List<CommentDto.CommentResponseDto> getComments(String userEmail, Long talkId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        List<Comment> comments = commentRepository.findAllByTalkId(talkId);

        return comments.stream().map(comment -> {
            int replyCount = commentRepository.countByParentId_Id(comment.getId()); // commentId와 일치하는 parentId 개수 반환
            // 좋아요 여부 확인
            boolean likedByCurrentUser = likeScrapRepository.existsByUserAndCommentAndPostType(user, comment, PostType.COMMENT);

            return CommentDto.CommentResponseDto.toDto(comment, replyCount, likedByCurrentUser);
        }).collect(Collectors.toList());
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

        // 댓글 수 업데이트
        talk.get().setReplyCount(talk.get().getReplyCount() + 1);
        talkRepository.save(talk.get());

        // 코인 부여x - 대댓글은 안하기로
//        user.get().setCoin(user.get().getCoin() + 2);

        // 알림 전송
        sendNotification(savedComment.getTalk().getUser(), savedComment);

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

//    private void checkAndAddCoins(User user) {
//        long totalInteractions = commentRepository.countByUser(user);
//
//        // 5의 배수일 때 코인 부여
//        if (totalInteractions % 5 == 0) {
//            user.setCoin(user.getCoin() + 10);
//            userRepository.save(user);
//        }
//    }

    public boolean checkAdoptionStatus(Long talkId) {
        Optional<Comment> adoptedComment = commentRepository.findByTalkIdAndAdoptionStatus(talkId, AdoptionStatus.ADOPTED);
        return adoptedComment.isPresent();  // 채택된 댓글이 존재하면 true
    }

    @Transactional
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

        Long talkId = comment.getTalk().getId();
        if (checkAdoptionStatus(talkId)) {
            throw new IllegalStateException("답변 채택은 게시글 당 하나만 가능합니다.");
        }

        comment.setAdoptionStatus(AdoptionStatus.ADOPTED);
        Comment savedComment = commentRepository.save(comment);

        // 채택된 사람한테 10코인
        comment.getUser().setCoin(comment.getUser().getCoin() + 10);
        userRepository.save(comment.getUser());

        return CommentDto.CommentResponseDto.toDto(savedComment);
    }

    public boolean checkIfUserAdopted(Long talkId) {
        return checkAdoptionStatus(talkId);
    }
}
