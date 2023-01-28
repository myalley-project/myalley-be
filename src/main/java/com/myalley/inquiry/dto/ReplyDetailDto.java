package com.myalley.inquiry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
@Setter
public class ReplyDetailDto {

    private Long replyId;

    private String reply;

    private LocalDate createdAt;


}
