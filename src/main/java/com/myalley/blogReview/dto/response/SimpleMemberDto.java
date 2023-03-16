package com.myalley.blogReview.dto.response;

import com.myalley.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMemberDto {
    private Long memberId;
    private String nickname;
    private String memberImage;

    public static SimpleMemberDto from(Member member){
        return new SimpleMemberDto(member.getMemberId(), member.getNickname(), member.getMemberImage());
    }
}
