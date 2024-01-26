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
    public CommentDto createComment(@RequestBody CommentDto commentDto) {
        String userEmail = SecurityUtil.getCurrentUsername();

        return commentService.createComment(commentDto, userEmail);
    }

    @PatchMapping("/{comment-id}") //수정
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }

    @DeleteMapping("/{comment-id}") //삭제
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

//    @PostMapping("/{comment-id}/replies") //대댓글 작성
//    public CommentDto createReply(@PathVariable Long parentId, @RequestBody CommentDto commentDto) {
//        return commentService.createReply(parentId, commentDto);
//    }

    @GetMapping("/comments/{comment-id}") //댓글 조회
    public List<CommentDto> getComments(@PathVariable Long postId) {
        return commentService.getComments(postId);
    }
}