package com.myalley.simpleReview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SimpleResponseDto {
    @Data
    @NoArgsConstructor
    public static class GetSimpleResponseDto{
        private Long id;
        private LocalDate viewDate;
        private Integer rate;
        private String content;
        private String time;
        private String congestion;
        private SimpleMemberResponseDto memberInfo; //전시회 상세에서 조회할 때 필요한 것
        private SimpleExhibitionResponseDto exhibitionInfo; //마이페이지에서 필요한 것
    }

    @Data
    @NoArgsConstructor
    public static class SimpleMemberResponseDto{
        private Long memberId;
        private String nickname;
        private String UserImage;
    }

    @Data
    @NoArgsConstructor
    public static class SimpleExhibitionResponseDto{
        private Long id;
        private String title;
    }
}
