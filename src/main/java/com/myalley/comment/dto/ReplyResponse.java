package com.myalley.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyResponse {
    private Long id;
    private String nickname;
    private String profileImg;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public static ReplyResponse of(Comment reply) {
        return new ReplyResponse(reply.getId(), reply.getMember().getNickname(),
                reply.getMember().getMemberImage(), reply.getMessage(), reply.getCreatedAt());
    }
}
