package com.myalley.inquiry.dto;

import lombok.*;

import java.time.LocalDate;
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDetailDto {
    private Long inquiryId;

    private String type;

    private String content;

    private LocalDate createdAt;

    private ReplyDetailDto reply;

    private MemberDetailDto member;


}
