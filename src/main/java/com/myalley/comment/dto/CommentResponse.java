package com.myalley.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private String nickname;
    private String profileImg;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private boolean deleted;
    private List<ReplyResponse> replies;

    public static CommentResponse of(Comment comment, List<ReplyResponse> replyResponses) {
        return new CommentResponse(comment.getId(), comment.getMember().getNickname(),
                comment.getMember().getMemberImage(), comment.getMessage(), comment.getCreatedAt(), comment.isDeleted(),
                replyResponses);
    }

//    public static CommentResponse softRemovedOf(Comment comment, List<ReplyResponse> replyResponses) {
//        return new CommentResponse(comment.getId(), null, null, null,
//                null, replyResponses);
//    }
}
