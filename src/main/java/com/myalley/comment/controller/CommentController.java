package com.myalley.comment.controller;

import com.myalley.comment.dto.CommentsResponse;
import com.myalley.comment.dto.CommentRequest;
import com.myalley.comment.service.CommentService;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Log
@RestController
@RequiredArgsConstructor
@RequestMapping(produces = "application/json; charset=utf8")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/mates/{mateId}/comments")
    public ResponseEntity addComment(@PathVariable Long mateId,
                                     @Valid @RequestBody CommentRequest request) {
        log.info("메이트 모집글 댓글 등록");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        commentService.addComment(mateId, request, memberId);

        return new ResponseEntity<>("댓글이 등록되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/api/comments/{commentId}/reply")
    public ResponseEntity addReply(@PathVariable Long commentId,
                                   @Valid @RequestBody CommentRequest request) {
        log.info("메이트 모집글 대댓글 등록");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        commentService.addReply(commentId, request, memberId);

        return new ResponseEntity<>("답글이 등록되었습니다.", HttpStatus.OK);
    }

    @GetMapping("/mates/{mateId}/comments")
    public ResponseEntity<CommentsResponse> findComments(@PathVariable Long mateId) {
        log.info("메이트 모집글의 댓글 목록 조회");

        CommentsResponse commentsResponse = commentService.findComments(mateId);
        return ResponseEntity.ok(commentsResponse);
    }

    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId) {
        log.info("메이트 모집글 댓글/대댓글 삭제");

        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        commentService.deleteComment(commentId, memberId);

        return new ResponseEntity<>("댓글이 삭제되었습니다.", HttpStatus.OK);
    }
}
