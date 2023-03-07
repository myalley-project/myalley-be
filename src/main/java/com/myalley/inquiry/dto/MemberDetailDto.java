package com.myalley.inquiry.dto;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDetailDto {

    private Long memberId;

    private String nickname;
}
