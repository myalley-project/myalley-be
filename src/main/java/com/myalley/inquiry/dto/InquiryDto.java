package com.myalley.inquiry.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDto {
    private String title;

    private String type;

    private String content;
}
