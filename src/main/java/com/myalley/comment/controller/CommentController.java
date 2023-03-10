package com.myalley.comment.controller;

import com.myalley.comment.dto.CommentsResponse;
import com.myalley.comment.dto.CommentRequest;
import com.myalley.comment.service.CommentService;
import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Log
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/mates/{id}/comments")
    public ResponseEntity addComment(@PathVariable(name = "id") Long mateId,
                                     @Valid @RequestBody CommentRequest request) {
        log.info("메이트 모집글 댓글 등록");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        commentService.addComment(mateId, request, memberId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("댓글이 등록되었습니다.", headers, HttpStatus.OK);
    }

    @PostMapping("/api/comments/{id}/reply")
    public ResponseEntity addReply(@PathVariable(name = "id") Long commentId,
                                   @Valid @RequestBody CommentRequest request) {
        log.info("메이트 모집글 대댓글 등록");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        commentService.addReply(commentId, request, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("답글이 등록되었습니다.", headers, HttpStatus.OK);
    }

    @PatchMapping("/api/mates/comments/{id}")
    public ResponseEntity updateComment(@PathVariable(name = "id") Long commentId,
                                        @Valid @RequestBody CommentRequest request) {
        log.info("메이트 모집글 댓글/대댓글 수정");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();

        if (request.getContent().isEmpty()) {
            throw new CustomException(MateExceptionType.COMMENT_NOT_FOUND);
        }

        commentService.updateComment(commentId, memberId, request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("댓글이 수정되었습니다.", headers, HttpStatus.OK);
    }


    @GetMapping("/mates/{id}/comments")
    public ResponseEntity<CommentsResponse> findComments(@PathVariable(name = "id") Long mateId) {
        log.info("메이트 모집글의 댓글 목록 조회");
        CommentsResponse commentsResponse = commentService.findComments(mateId);

        return ResponseEntity.ok(commentsResponse);
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity deleteComment(@PathVariable(name = "id") Long commentId) {
        log.info("메이트 모집글 댓글/대댓글 삭제");
        Member member = (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long memberId = member.getMemberId();
        commentService.deleteComment(commentId, memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        return new ResponseEntity<>("댓글이 삭제되었습니다.", headers, HttpStatus.OK);
    }
}
