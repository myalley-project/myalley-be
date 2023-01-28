package com.myalley.inquiry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Builder
@Setter
public class InquiryDetailDto {
    private Long inquiryId;

    private String type;

    private String content;

    private LocalDate createdAt;

    private ReplyDetailDto reply;

    private MemberDetailDto member;


}
