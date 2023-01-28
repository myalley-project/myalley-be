package com.myalley.inquiry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class InquiryDto {
    private String title;

    private String type;

    private String content;
}
