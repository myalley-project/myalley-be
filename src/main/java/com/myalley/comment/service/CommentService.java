package com.myalley.comment.service;

import com.myalley.comment.domain.Comment;
import com.myalley.comment.dto.*;
import com.myalley.comment.repository.CommentRepository;
import com.myalley.exception.CustomException;
import com.myalley.exception.MateExceptionType;
import com.myalley.exception.MemberExceptionType;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.repository.MateRepository;
import com.myalley.member.domain.Member;
import com.myalley.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final MateRepository mateRepository;

    //댓글 작성
    @Transactional
    public Long addComment(Long mateId, NewCommentRequest request, Long memberId) {
        Mate mate = mateRepository.findById(mateId)
                .orElseThrow(() -> new CustomException(MateExceptionType.MATE_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));

        Comment comment = Comment.parent(member, mate, request.getContent());
        commentRepository.save(comment);

        return comment.getId();
    }

    //대댓글 작성
    @Transactional
    public Long addReply(Long commentId, NewReplyRequest request, Long memberId) {
        Comment parent = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(MateExceptionType.COMMENT_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (!parent.isParent()) {
            throw new CustomException(MateExceptionType.CANNOT_WRITE_REPLY);
        }
        Mate mate = parent.getMate();

        Comment reply = Comment.child(member, mate, request.getContent(), parent);
        commentRepository.save(reply);

        return reply.getId();
    }

    public CommentsResponse findComments(Long mateId) {
        List<Comment> comments = commentRepository.findCommentsByMateId(mateId);
        List<CommentResponse> commentResponses = comments.stream()
                .map(c -> convertToCommentResponse(c))
                .collect(Collectors.toList());
//        int numOfComment = commentResponses.size();
//        int numOfReply = commentResponses.stream()
//                .map(c -> c.getReplies().size())
//                .reduce(Integer::sum).orElse(0);
        return new CommentsResponse(commentResponses);
    }

    private CommentResponse convertToCommentResponse(Comment comment) {
        if (comment.isSoftRemoved()) {
            return CommentResponse.softRemovedOf(comment, convertToReplyResponses(comment));
        }
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
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(MateExceptionType.COMMENT_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MemberExceptionType.NOT_FOUND_MEMBER));

        if (!member.equals(comment.getMember())) {
            throw new CustomException(MemberExceptionType.TOKEN_FORBIDDEN);
        }
        deleteCommentOrReply(comment);
//        comment.changePretendingToBeRemoved();
    }

    private void deleteCommentOrReply(Comment comment) {
        if (comment.isParent()) {
            deleteParent(comment);
            return;
        }
        deleteChild(comment);
    }

    private void deleteParent(Comment comment) {
        if (comment.hasNoReply()) {
            commentRepository.delete(comment);
            return;
        }
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

}
