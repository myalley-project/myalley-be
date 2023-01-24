package com.myalley.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewReplyRequest {
    @NotBlank(message = "댓글은 150자 이하여야합니다.")
    private String content;
}
