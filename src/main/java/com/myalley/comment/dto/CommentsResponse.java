package com.myalley.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponse {
    private List<CommentResponse> comments;
//    private int totalCount;
}
