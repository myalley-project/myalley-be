package com.myalley.blogReview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

public class BlogResponseDto {

    @Setter
    @NoArgsConstructor
    public static class SimpleBlogDto{
        private Long id;
        private String title;
        private String writer;
        private LocalDate viewDate;
        private Integer viewCount;
        private ImageResponseDto imageInfo;
    }

    @Data
    @NoArgsConstructor
    public static class DetailBlogDto {
        private Long id;
        private LocalDate viewDate;
        private String title;
        private String content;
        private Integer likeCount;
        private Integer viewCount;
        private Integer bookmarkCount;
        private String transportation;  //주차공간
        private String revisit;  //재방문의향
        private String congestion; //혼잡도
        private List<ImageResponseDto> images;

        private SimpleMemberDto memberInfo;
        private Long exhibitionId;
    }

    @Data
    @NoArgsConstructor
    public static class SimpleMemberDto {
        private Long id;
        private String nickname;
    }

    @Data
    @NoArgsConstructor
    public static class SimpleExhibitionDto {
        private Long id;
    }

}
