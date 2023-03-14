package com.myalley.blogReview.dto.response;

import com.myalley.common.dto.pagingDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class BlogResponseDtoXXX {

    @Data
    @NoArgsConstructor
    public static class BlogListDto{ //블로그 목록 조회 기본형
        private List<SimpleBlogDto> blogInfo;
        private pagingDto pageInfo;
    }

    @Data
    @NoArgsConstructor
    public static class UserBlogListDto{ //블로그 목록 조회 작성글형
        private List<SimpleUserBlogDto> blogInfo;
        private pagingDto pageInfo;
    }

    public static class DetailBlogDto {

    }


    public static class SimpleBlogDto{

    }


    public static class SimpleUserBlogDto{

    }

    //멤버에서 쓰기
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
