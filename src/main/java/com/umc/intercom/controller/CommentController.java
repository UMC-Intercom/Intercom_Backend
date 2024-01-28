package com.umc.intercom.controller;

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
        return commentService.createComment(commentDto);
    }

    @PatchMapping("/{id}") //수정
    public CommentDto updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        return commentService.updateComment(id, commentDto);
    }

    @DeleteMapping("/{id}") //삭제
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

//    @PostMapping("/{comment-id}/replies") //대댓글 작성
//    public CommentDto createReply(@PathVariable Long parentId, @RequestBody CommentDto commentDto) {
//        return commentService.createReply(parentId, commentDto);
//    }

    @GetMapping("/talk/{talkId}") //댓글 조회
    public List<CommentDto> getComments(@PathVariable Long talkId) {
        return commentService.getComments(talkId);
    }
}