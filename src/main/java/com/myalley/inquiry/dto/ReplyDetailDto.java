package com.myalley.inquiry.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDetailDto {

    private Long replyId;

    private String reply;

    private String replier;

    private LocalDate createdAt;


}
