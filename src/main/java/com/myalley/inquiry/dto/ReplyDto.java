package com.myalley.inquiry.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {
    private Long inquiryId;

    private String reply;
}
