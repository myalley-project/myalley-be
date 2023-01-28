package com.myalley.inquiry.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Setter
public class MemberDetailDto {

    private String memberId;

    private String nickname;
}
