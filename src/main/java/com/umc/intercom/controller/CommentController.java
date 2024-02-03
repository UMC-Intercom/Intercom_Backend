package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.CommentDto;
import com.umc.intercom.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping  //작성
    public CommentDto.CommentResponseDto createComment(@RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        String userEmail = SecurityUtil.getCurrentUsername();   // 현재 로그인한 사용자 이메일
        return commentService.createComment(userEmail, commentRequestDto);
    }

    @PatchMapping("/{id}") //수정
    public CommentDto.CommentResponseDto  updateComment(@PathVariable Long id, @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        return commentService.updateComment(id, commentRequestDto);
    }

    @DeleteMapping("/{id}") //삭제
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @GetMapping("/talk/{talkId}") //댓글 조회
    public List<CommentDto.CommentResponseDto> getComments(@PathVariable Long talkId) {
        return commentService.getComments(talkId);
    }

    @PostMapping("/reply")
    public CommentDto.CommentResponseDto createReplyComment(@RequestBody CommentDto.ReplyRequestDto replyRequestDto) {
        String userEmail = SecurityUtil.getCurrentUsername();
        return commentService.createReplyComment(userEmail, replyRequestDto);
    }
}