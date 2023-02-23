package com.myalley.blogReview.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.common.dto.pagingDto;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Data
    @NoArgsConstructor
    public static class UserBlogListDto{
        private List<SimpleUserBlogDto> blogInfo;
        private pagingDto pageInfo;
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
        private String time;
        private Integer likeCount;
        private Integer viewCount;
        private Integer bookmarkCount;
        private String transportation;
        private String revisit;
        private String congestion;
        private boolean likeStatus;
        private boolean bookmarkStatus;
        private List<ImageResponseDto> imageInfo;

        private SimpleMemberDto memberInfo;
        private SimpleExhibitionDto exhibitionInfo;
    }

    @Data
    @NoArgsConstructor
    public static class SimpleBlogDto{
        private Long id;
        private String title;
        private String writer;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate viewDate;
        private Integer viewCount;
        private ImageResponseDto imageInfo;
    }

    @Data
    @NoArgsConstructor
    public static class SimpleUserBlogDto{
        private Long id;
        private String title;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        private LocalDate viewDate;
        private Integer viewCount;
        private ImageResponseDto imageInfo;
    }

    @Data
    @NoArgsConstructor
    public static class SimpleMemberDto {
        private Long memberId;
        private String nickname;
        private String memberImage;
    }

    @Data
    @NoArgsConstructor
    public static class SimpleExhibitionDto {
        private Long id;
        private String title;
        private String posterUrl;
        private String duration;
        private String space;
        private String type;
    }

}
