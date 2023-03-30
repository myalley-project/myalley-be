package com.myalley.comment.service;

import com.myalley.comment.domain.Comment;
import com.myalley.comment.dto.*;
import com.myalley.comment.repository.CommentRepository;
import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.exception.MemberExceptionType;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.service.MateService;
import com.myalley.member.domain.Member;
import com.myalley.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final MateService mateService;

    //댓글 작성
    @Transactional
    public void createComment(Long mateId, CommentRequest request, Long memberId) {
        Mate mate = mateService.validateExistMate(mateId);
        Member member = memberService.validateMember(memberId);
        Comment comment = Comment.parent(member, mate, request.getContent());
        commentRepository.save(comment);
    }

    //대댓글 작성
    @Transactional
    public void createReply(Long commentId, CommentRequest request, Long memberId) {
        Comment parent = validateExistComment(commentId);
        Member member = memberService.validateMember(memberId);

        if (!parent.isParent()) {
            throw new CustomException(MateExceptionType.CANNOT_WRITE_REPLY);
        }
        Mate mate = parent.getMate();

        Comment reply = Comment.child(member, mate, request.getContent(), parent);
        commentRepository.save(reply);
    }
    
    public CommentsResponse findCommentsByMateId(Long mateId) {
        List<Comment> comments = commentRepository.findCommentsByMateId(mateId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());

        return new CommentsResponse(commentResponses);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        return CommentResponse.of(comment, convertToReplyResponses(comment));
    }

    private List<ReplyResponse> convertToReplyResponses(Comment parent) {
        List<Comment> replies = commentRepository.findRepliesByParent(parent);
        List<ReplyResponse> replyResponses = new ArrayList<>();
        for (Comment reply : replies) {
            replyResponses.add(ReplyResponse.of(reply));
        }
        return replyResponses;
    }

    @Transactional
    public void removeByCommentIdAndMemberId(Long commentId, Long memberId) {
        if (verifyCommentAuthor(commentId, memberId)) {
            commentRepository.deleteById(commentId);
        }
    }

    //댓글 존재여부 검증
    private Comment validateExistComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(MateExceptionType.COMMENT_NOT_FOUND));
    }

    //댓글 작성자 본인 확인
    private boolean verifyCommentAuthor(Long commentId, Long memberId) {
        Comment comment = validateExistComment(commentId);
        Member member = memberService.validateMember(memberId);

        if (!member.equals(comment.getMember())) {
            throw new CustomException(MemberExceptionType.TOKEN_FORBIDDEN);
        }
        return true;
    }
}
