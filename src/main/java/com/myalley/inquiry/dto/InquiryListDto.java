package com.myalley.inquiry.dto;

import java.time.LocalDate;

public class InquiryListDto {
    private Long inquiryId;

    private String type;

    private String title;

    private boolean isAnswered;

    private LocalDate createdAt;

    private MemberDetailDto memberDetailDto;

}
