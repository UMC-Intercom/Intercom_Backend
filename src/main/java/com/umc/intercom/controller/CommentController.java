package com.umc.intercom.controller;

import com.umc.intercom.config.security.SecurityUtil;
import com.umc.intercom.dto.CommentDto;
import com.umc.intercom.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Operation(summary = "톡톡 게시글에 댓글 작성")
    @PostMapping  //작성
    public CommentDto.CommentResponseDto createComment(@RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        String userEmail = SecurityUtil.getCurrentUsername();   // 현재 로그인한 사용자 이메일
        return commentService.createComment(userEmail, commentRequestDto);
    }

    @Operation(summary = "댓글 수정", description = "{id} 자리에 수정할 댓글 id를 전달해주세요.")
    @PatchMapping("/{id}") //수정
    public CommentDto.CommentResponseDto  updateComment(@PathVariable Long id, @RequestBody CommentDto.CommentRequestDto commentRequestDto) {
        return commentService.updateComment(id, commentRequestDto);
    }

    @Operation(summary = "댓글 삭제", description = "{id} 자리에 삭제할 댓글 id를 전달해주세요.")
    @DeleteMapping("/{id}") //삭제
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @Operation(summary = "톡톡 게시글의 댓글 조회", description = "{talkId} 자리에 톡톡 게시글 id를 전달해주세요.")
    @GetMapping("/talk/{talkId}") //댓글 조회
    public List<CommentDto.CommentResponseDto> getComments(@PathVariable Long talkId) {
        return commentService.getComments(talkId);
    }

    @Operation(summary = "대댓글 작성")
    @PostMapping("/reply")
    public CommentDto.CommentResponseDto createReplyComment(@RequestBody CommentDto.ReplyRequestDto replyRequestDto) {
        String userEmail = SecurityUtil.getCurrentUsername();
        return commentService.createReplyComment(userEmail, replyRequestDto);
    }
}