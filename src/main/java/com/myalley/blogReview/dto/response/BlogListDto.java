package com.myalley.blogReview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.blogReview.domain.BlogReview;
import org.springframework.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class BlogListDto {
    private Long id;
    private String title;
    private String writer;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate viewDate;
    private Integer viewCount;
    private ImageDto imageInfo;

    //기본형 - writer 有
    public static BlogListDto basicFrom(BlogReview blog){
        if(!CollectionUtils.isEmpty(blog.getImages()))
            return new BlogListDto(blog.getId(), blog.getTitle(), blog.getMember().getNickname(), blog.getViewDate(),
                    blog.getViewCount(), ImageDto.from(blog.getImages().get(0)));
        else return new BlogListDto(blog.getId(), blog.getTitle(), blog.getMember().getNickname(), blog.getViewDate(),
                blog.getViewCount(), null);
    }

    //작성글형 - writer 無
    public static BlogListDto selfFrom(BlogReview blog){
        if(!CollectionUtils.isEmpty(blog.getImages()))
            return new BlogListDto(blog.getId(), blog.getTitle(), null, blog.getViewDate(), blog.getViewCount(),
                    ImageDto.from(blog.getImages().get(0)));
        else return new BlogListDto(blog.getId(), blog.getTitle(), null, blog.getViewDate(), blog.getViewCount(),
                null);
    }
}
