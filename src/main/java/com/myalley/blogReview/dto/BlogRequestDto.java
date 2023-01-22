package com.myalley.blogReview.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BlogRequestDto {

    @Data
    @NoArgsConstructor
    public static class PostBlogDto {
        private Long exhibition;
        private String viewDate;
        private String time;
        private String title;
        private String content;
        private String transportation;
        private String revisit;
        private String congestion;
    }

    @Data
    @NoArgsConstructor
    public static class PutBlogDto {
        //전시회를 변경할 수 있게 할지 말지 생각하는 중.
        //과연 기존 전시회 블로그로 썼던 글을 다른 전시회로 쓰려고 할까?
        //지금으로써는 안 바꿀 것 같음
        //private Long exhibition;
        private String viewDate;
        private String time;
        private String title;
        private String content;
        private String transportation;
        private String revisit;
        private String congestion;
    }
}
