package com.myalley.blogReview.dto.response;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.common.dto.pagingDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BlogListResponseDto {
    //블로그 목록 조회시 모든 response dto 형
    private List<BlogListDto> blogInfo;
    private pagingDto pageInfo;

    public static BlogListResponseDto blogOf(Page<BlogReview> blogReviewPage, String type){
        BlogListResponseDto listResponseDto = new BlogListResponseDto();
        if(type.equals("basic"))
            listResponseDto.setBlogInfo(blogReviewPage.get().map(BlogListDto::basicFrom).collect(Collectors.toList()));
        else if(type.equals("self"))
            listResponseDto.setBlogInfo(blogReviewPage.get().map(BlogListDto::selfFrom).collect(Collectors.toList()));
        listResponseDto.setPageInfo(new pagingDto(blogReviewPage.getNumber()+1, blogReviewPage.getSize(),
                blogReviewPage.getTotalElements(), blogReviewPage.getTotalPages()));
        return listResponseDto;
    }

    public static BlogListResponseDto likesFrom(Page<BlogLikes> blogLikesPage){
        BlogListResponseDto listResponseDto = new BlogListResponseDto();
        listResponseDto.setBlogInfo(blogLikesPage.get().map(blogLikes -> BlogListDto.basicFrom(blogLikes.getBlog()))
                    .collect(Collectors.toList()));
        listResponseDto.setPageInfo(new pagingDto(blogLikesPage.getNumber()+1, blogLikesPage.getSize(),
                blogLikesPage.getTotalElements(), blogLikesPage.getTotalPages()));
        return listResponseDto;
    }

    public static BlogListResponseDto bookmarkFrom(Page<BlogBookmark> blogBookmarkPage){
        BlogListResponseDto listResponseDto = new BlogListResponseDto();
        listResponseDto.setBlogInfo(blogBookmarkPage.get().map(blogBookmark -> BlogListDto.basicFrom(blogBookmark.getBlog()))
                .collect(Collectors.toList()));
        listResponseDto.setPageInfo(new pagingDto(blogBookmarkPage.getNumber()+1, blogBookmarkPage.getSize(),
                blogBookmarkPage.getTotalElements(), blogBookmarkPage.getTotalPages()));
        return listResponseDto;
    }

}
