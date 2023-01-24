package com.myalley.simpleReview.dto;

import com.myalley.common.dto.pagingDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class SimpleResponseDto {
    //(Test) 개별 조회
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

    //전시 상세페이지 조회
    @Data
    @NoArgsConstructor
    public static class ListExhibitionSimpleResponseDto{
        private List<ExhibitionSimpleReviewResponseDto> simpleInfo;
        private pagingDto pageInfo;
    }

    @Data
    @NoArgsConstructor
    public static class ExhibitionSimpleReviewResponseDto {
        private Long id;
        private LocalDate viewDate;
        private Integer rate;
        private String content;
        private String time;
        private String congestion;
        private SimpleMemberResponseDto memberInfo;
    }

    //마이페이지 조회
    @Data
    @NoArgsConstructor
    public static class ListUserSimpleResponseDto{
        private List<UserSimpleReviewResponseDto> simpleInfo;
        private pagingDto pageInfo;
    }
    @Data
    @NoArgsConstructor
    public static class UserSimpleReviewResponseDto {
        private Long id;
        private LocalDate viewDate;
        private Integer rate;
        private String content;
        private String time;
        private String congestion;
        private SimpleExhibitionResponseDto exhibitionInfo;
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
