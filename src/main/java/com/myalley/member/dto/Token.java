package com.myalley.member.dto;

import lombok.Data;

@Data
public class Token {
    private String accessToken;

    private String refreshToken;
}
