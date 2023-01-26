package com.myalley.member.dto;

import com.myalley.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberMateDto {
    private Long memberId;

    private String memberNickname;

    private String memberProfileImg;

    private String memberGender;

    private String memberAge;


    private static MemberMateDto of(Member member){
        return new MemberMateDto(member.getMemberId(),member.getNickname(),
                member.getMemberImage(),member.getGender().getKey(),(member.getBirth().toString()).substring(0,4));
    }
}
