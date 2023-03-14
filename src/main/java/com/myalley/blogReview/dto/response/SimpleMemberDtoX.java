package com.myalley.blogReview.dto.response;

import com.myalley.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleMemberDtoX {
    private Long memberId;
    private String nickname;
    private String memberImage;

    public static SimpleMemberDtoX from(Member member){
        return new SimpleMemberDtoX(member.getMemberId(), member.getNickname(), member.getMemberImage());
    }
}
