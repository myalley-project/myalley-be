package com.myalley.member.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class AdminMemberInfoDto {

    private Long memberId;

    private String email;

    private String nickname;

    private String level;

    private String status;

    private String blackStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate createdAt;
}
