package com.myalley.blogReview.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BlogRequestDto {

    @Data
    @NoArgsConstructor
    public static class PostBlogDto {
        @NotNull
        private String viewDate;
        @NotNull
        private String title;
        @NotNull
        private String content;
        @NotNull
        private String time;
        private String transportation;
        private String revisit;
        private String congestion;
    }

    @Data
    @NoArgsConstructor
    public static class PutBlogDto {
        @NotNull
        private String viewDate;
        @NotNull
        private String title;
        @NotNull
        private String content;
        @NotNull
        private String time;
        private String transportation;
        private String revisit;
        private String congestion;
    }
}
