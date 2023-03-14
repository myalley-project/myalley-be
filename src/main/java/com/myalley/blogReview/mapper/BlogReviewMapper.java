package com.myalley.blogReview.mapper;


import com.myalley.blogReview.domain.*;
import com.myalley.blogReview.dto.request.BlogRequestDto;
import com.myalley.blogReview.dto.response.*;
import com.myalley.common.dto.pagingDto;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


@Mapper
public interface BlogReviewMapper {
    BlogReviewMapper INSTANCE = Mappers.getMapper(BlogReviewMapper.class);

    //** REQUEST **
    //1. 블로그 등록
    default BlogReview postBlogDtoToBlogReview(BlogRequestDto requestDto){
        BlogReview.BlogReviewBuilder blogReview = BlogReview.builder();

        blogReview.title( requestDto.getTitle() );
        blogReview.content( requestDto.getContent() );
        blogReview.viewDate( LocalDate.parse( requestDto.getViewDate() ) );
        blogReview.time( requestDto.getTime() );

        if(requestDto.getRevisit() == null)
            blogReview.revisit( "모르겠음" );
        else
            blogReview.revisit( requestDto.getRevisit() );
        if(requestDto.getCongestion() == null)
            blogReview.congestion( "기억나지않음" );
        else
            blogReview.congestion( requestDto.getCongestion() );
        if(requestDto.getTransportation() == null)
            blogReview.transportation( "기억나지않음" );
        else
            blogReview.transportation( requestDto.getTransportation() );

        return blogReview.build();
    }
    //2. 블로그 수정
    BlogReview putBlogDtoToBlogReview(BlogRequestDto requestDto);
    
    //** RESPONSE **
    //1. 블로그 상세
    default BlogDetailResponseDto blogToDetailBlogDto(DetailBlogReview blog){
        BlogDetailResponseDto dto = new BlogDetailResponseDto();
        dto.setId(blog.getBlogReview().getId());
        dto.setTitle(blog.getBlogReview().getTitle());
        dto.setContent(blog.getBlogReview().getContent());
        dto.setTime(blog.getBlogReview().getTime());
        dto.setLikeCount(blog.getBlogReview().getLikeCount());
        dto.setBookmarkCount(blog.getBlogReview().getBookmarkCount());
        dto.setTransportation(blog.getBlogReview().getTransportation());
        dto.setCongestion(blog.getBlogReview().getCongestion());
        dto.setRevisit(blog.getBlogReview().getRevisit());
        dto.setViewDate(blog.getBlogReview().getViewDate());
        dto.setCreatedAt(blog.getBlogReview().getCreatedAt());
        dto.setViewCount(blog.getBlogReview().getViewCount());
        dto.setImageInfo(imageToImageDtoList(blog.getBlogReview().getImages()));
        //dto.setMemberInfo(memberToSimpleMemberDto(blog.getBlogReview().getMember()));
        //dto.setExhibitionInfo(exhibitionToSimpleExhibitionDto(blog.getBlogReview().getExhibition()));
        dto.setLikeStatus(blog.isLikesStatus());
        dto.setBookmarkStatus(blog.isBookmarkStatus());

        return dto;
    }
    //2. 블로그 목록 : 전시 관련
    default BlogListResponseDto pageableBlogToBlogListDto(Page<BlogReview> pageBlogs){
        BlogListResponseDto dto = new BlogListResponseDto();
        List<BlogListDto> simpleBlogDtoList = pageBlogs.get().map(blogReview -> {
            BlogListDto simpleBlogDto = new BlogListDto();
            simpleBlogDto.setId(blogReview.getId());
            simpleBlogDto.setViewDate(blogReview.getViewDate());
            if(!CollectionUtils.isEmpty(blogReview.getImages()))
                simpleBlogDto.setImageInfo(imageListToImageDto(blogReview.getImages()));
            simpleBlogDto.setTitle(blogReview.getTitle());
            simpleBlogDto.setWriter(blogReview.getMember().getNickname());
            simpleBlogDto.setViewCount(blogReview.getViewCount());
            return simpleBlogDto;
        }).collect(Collectors.toList());
        pagingDto paging = new pagingDto(pageBlogs.getNumber()+1, pageBlogs.getSize(),
                pageBlogs.getTotalElements(), pageBlogs.getTotalPages());
        dto.setBlogInfo(simpleBlogDtoList);
        dto.setPageInfo(paging);
        return dto;
    }
    //3. 블로그 목록 : 내 블로그
    default BlogListResponseDto pageableBlogToUserBlogListDto(Page<BlogReview> pageBlogs){
        BlogListResponseDto dto = new BlogListResponseDto();
        List<BlogListDto> simpleBlogDtoList = pageBlogs.get().map(blogReview -> {
            BlogListDto userBlogDto = new BlogListDto();
            userBlogDto.setId(blogReview.getId());
            userBlogDto.setViewDate(blogReview.getViewDate());
            if(!CollectionUtils.isEmpty(blogReview.getImages()))
                userBlogDto.setImageInfo(imageListToImageDto(blogReview.getImages()));
            userBlogDto.setTitle(blogReview.getTitle());
            userBlogDto.setViewCount(blogReview.getViewCount());
            return userBlogDto;
        }).collect(Collectors.toList());
        pagingDto paging = new pagingDto(pageBlogs.getNumber()+1, pageBlogs.getSize(),
                pageBlogs.getTotalElements(), pageBlogs.getTotalPages());
        dto.setBlogInfo(simpleBlogDtoList);
        dto.setPageInfo(paging);
        return dto;
    }
    //4. 블로그 목록 : 마이페이지 좋아요
    default BlogListResponseDto pageableLikesToMyBlogLikesDto(Page<BlogLikes> pageLikes){
        BlogListResponseDto dto = new BlogListResponseDto();
        List<BlogListDto> simpleBlogDtoList = pageLikes.get().map(blogLikes -> {
            BlogListDto simpleBlogDto = new BlogListDto();
            simpleBlogDto.setId(blogLikes.getBlog().getId());
            simpleBlogDto.setViewDate(blogLikes.getBlog().getViewDate());
            if(!CollectionUtils.isEmpty(blogLikes.getBlog().getImages()))
                simpleBlogDto.setImageInfo(imageListToImageDto(blogLikes.getBlog().getImages()));
            simpleBlogDto.setTitle(blogLikes.getBlog().getTitle());
            simpleBlogDto.setWriter(blogLikes.getBlog().getMember().getNickname());
            simpleBlogDto.setViewCount(blogLikes.getBlog().getViewCount());
            return simpleBlogDto;
        }).collect(Collectors.toList());
        pagingDto paging = new pagingDto(pageLikes.getNumber()+1, pageLikes.getSize(),
                pageLikes.getTotalElements(), pageLikes.getTotalPages());
        dto.setBlogInfo(simpleBlogDtoList);
        dto.setPageInfo(paging);
        return dto;
    }
    //5. 블로그 목록 : 마이페이지 북마크
    default BlogListResponseDto pageableBookmarkToMyBlogBookmarkDto(Page<BlogBookmark> bookmarkPage){
        BlogListResponseDto dto = new BlogListResponseDto();
        List<BlogListDto> simpleBlogDtoList = bookmarkPage.get().map(blogBookmark -> {
            BlogListDto simpleBlogDto = new BlogListDto();
            simpleBlogDto.setId(blogBookmark.getBlog().getId());
            simpleBlogDto.setViewDate(blogBookmark.getBlog().getViewDate());
            if(!CollectionUtils.isEmpty(blogBookmark.getBlog().getImages()))
                simpleBlogDto.setImageInfo(imageListToImageDto(blogBookmark.getBlog().getImages()));
            simpleBlogDto.setTitle(blogBookmark.getBlog().getTitle());
            simpleBlogDto.setWriter(blogBookmark.getBlog().getMember().getNickname());
            simpleBlogDto.setViewCount(blogBookmark.getBlog().getViewCount());
            return simpleBlogDto;
        }).collect(Collectors.toList());
        pagingDto paging = new pagingDto(bookmarkPage.getNumber()+1, bookmarkPage.getSize(),
                bookmarkPage.getTotalElements(), bookmarkPage.getTotalPages());
        dto.setBlogInfo(simpleBlogDtoList);
        dto.setPageInfo(paging);
        return dto;
    }

    //** 그 외 **
    //1. 멤버 DTO
    BlogResponseDtoXXX.SimpleMemberDto memberToSimpleMemberDto(Member member);
    //2. 전시회 DTO
    BlogResponseDtoXXX.SimpleExhibitionDto exhibitionToSimpleExhibitionDto(Exhibition exhibition);
    //3. 이미지 하나 DTO
    ImageDto imageToImageDto(BlogImage image);
    //4. 이미지 리스트 중 대표사진 DTO
    default ImageDto imageListToImageDto(List<BlogImage> imageList){
        return imageToImageDto(imageList.get(0));
    }
    //5. 이미지 리스트 전체 조회 DTO
    default List<ImageDto> imageToImageDtoList(List<BlogImage> images){
        List<ImageDto> imageDtoList = images.stream().map(i ->{
            ImageDto dto = imageToImageDto(i);
            return dto;
        }).collect(Collectors.toList());
        return imageDtoList;
    }
}
