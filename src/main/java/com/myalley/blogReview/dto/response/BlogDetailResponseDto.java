package com.myalley.blogReview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.exhibition.dto.response.ExhibitionBlogDto;
import com.myalley.member.dto.MemberBlogDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class BlogDetailResponseDto {
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
    private List<ImageDto> imageInfo;

    private MemberBlogDto memberInfo;
    private ExhibitionBlogDto exhibitionInfo;

    public static BlogDetailResponseDto of(BlogReview blog, boolean likeStatus, boolean bookmarkStatus){
        return new BlogDetailResponseDto(blog.getId(),blog.getViewDate(),blog.getCreatedAt(),
                blog.getTitle(),blog.getContent(),blog.getTime(), blog.getLikeCount(), blog.getViewCount(),
                blog.getBookmarkCount(), blog.getTransportation(), blog.getRevisit(), blog.getCongestion(),
                likeStatus,bookmarkStatus,blog.getImages().stream().map(ImageDto::from).collect(Collectors.toList()),
                MemberBlogDto.from(blog.getMember()), ExhibitionBlogDto.from(blog.getExhibition()));
    }
}
