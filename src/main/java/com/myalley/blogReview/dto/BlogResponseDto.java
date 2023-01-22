package com.myalley.blogReview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.common.dto.pagingDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BlogResponseDto {

    @Data
    @NoArgsConstructor
    public static class BlogListDto{
        private List<SimpleBlogDto> blogInfo;
        private pagingDto pageInfo;
    }

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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate viewDate;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDateTime createdAt;
        private String title;
        private String content;
        private Integer likeCount;
        private Integer viewCount;
        private Integer bookmarkCount;
        private String transportation;  //주차공간
        private String revisit;  //재방문의향
        private String congestion; //혼잡도
        private List<ImageResponseDto> imageInfo;

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
