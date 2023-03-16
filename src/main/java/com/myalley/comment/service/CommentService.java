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
    public void addComment(Long mateId, CommentRequest request, Long memberId) {
        Mate mate = mateService.verifyMate(mateId);
        Member member = memberService.verifyMember(memberId);
        Comment comment = Comment.parent(member, mate, request.getContent());
        commentRepository.save(comment);
    }

    //대댓글 작성
    @Transactional
    public void addReply(Long commentId, CommentRequest request, Long memberId) {
        Comment parent = verifyComment(commentId);
        Member member = memberService.verifyMember(memberId);

        if (!parent.isParent()) {
            throw new CustomException(MateExceptionType.CANNOT_WRITE_REPLY);
        }
        Mate mate = parent.getMate();

        Comment reply = Comment.child(member, mate, request.getContent(), parent);
        commentRepository.save(reply);
    }
    
    public CommentsResponse findComments(Long mateId) {
        List<Comment> comments = commentRepository.findCommentsByMateId(mateId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());

        return new CommentsResponse(commentResponses);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
//        if (comment.isSoftRemoved()) {
//            return CommentResponse.softRemovedOf(comment, convertToReplyResponses(comment));
//        }
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
    public void deleteComment(Long commentId, Long memberId) {
        Comment comment = verifyComment(commentId);
        Member member = memberService.verifyMember(memberId);

        if (!member.equals(comment.getMember())) {
            throw new CustomException(MemberExceptionType.TOKEN_FORBIDDEN);
        }
        deleteCommentOrReply(comment);
    }

    private void deleteCommentOrReply(Comment comment) {
            deleteParent(comment);
    }

    private void deleteParent(Comment comment) {
        comment.changePretendingToBeRemoved();
    }

    private void deleteChild(Comment comment) {
        Comment parent = comment.getParent();
        parent.deleteChild(comment);
        commentRepository.delete(comment);

        if (parent.hasNoReply() && parent.isSoftRemoved()) {
            commentRepository.delete(parent);
        }
    }

    //댓글 존재여부 검증
    private Comment verifyComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(MateExceptionType.COMMENT_NOT_FOUND));
    }
}
