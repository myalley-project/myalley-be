package com.myalley.blogReview.mapper;

import com.myalley.blogReview.domain.BlogBookmark;
import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogLikes;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogRequestDto;
import com.myalley.blogReview.dto.BlogResponseDto;
import com.myalley.blogReview.dto.ImageResponseDto;
import com.myalley.common.dto.pagingDto;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


@Mapper
public interface BlogReviewMapper {
    BlogReviewMapper INSTANCE = Mappers.getMapper(BlogReviewMapper.class);

    //** REQUEST **
    //1. 블로그 등록
    BlogReview postBlogDtoToBlogReview(BlogRequestDto.PostBlogDto requestDto);
    //2. 블로그 수정
    BlogReview putBlogDtoToBlogReview(BlogRequestDto.PutBlogDto requestDto);
    
    //** RESPONSE **
    //1. 블로그 상세
    default BlogResponseDto.DetailBlogDto blogToDetailBlogDto(BlogReview blog){
        BlogResponseDto.DetailBlogDto dto = new BlogResponseDto.DetailBlogDto();
        dto.setId(blog.getId());
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setLikeCount(blog.getLikeCount());
        dto.setBookmarkCount(blog.getBookmarkCount());
        dto.setTransportation(blog.getTransportation());
        dto.setCongestion(blog.getCongestion());
        dto.setRevisit(blog.getRevisit());
        dto.setViewDate(blog.getViewDate());
        dto.setCreatedAt(blog.getCreatedAt());
        dto.setViewCount(blog.getViewCount());
        dto.setImageInfo(imageToImageDtoList(blog.getImages()));
        dto.setMemberInfo(memberToSimpleMemberDto(blog.getMember()));
        dto.setExhibitionInfo(exhibitionToSimpleExhibitionDto(blog.getExhibition()));

        return dto;
    }
    //2. 블로그 목록 : 전시 관련
    default BlogResponseDto.BlogListDto pageableBlogToBlogListDto(Page<BlogReview> pageBlogs){
        BlogResponseDto.BlogListDto dto = new BlogResponseDto.BlogListDto();
        List<BlogResponseDto.SimpleBlogDto> simpleBlogDtoList = pageBlogs.get().map( blogReview -> {
            BlogResponseDto.SimpleBlogDto simpleBlogDto = new BlogResponseDto.SimpleBlogDto();
            simpleBlogDto.setId(blogReview.getId());
            simpleBlogDto.setViewDate(blogReview.getViewDate());
            if(!CollectionUtils.isEmpty(blogReview.getImages()))
                simpleBlogDto.setImageInfo(imageListToImageDto(blogReview.getImages()));
                //simpleBlogDto.setImageInfo(imageToImageDto(blogReview.getImages().get(0))); //이미지가 없는 건 일단 Null이 들어감
            simpleBlogDto.setTitle(blogReview.getTitle());
            simpleBlogDto.setWriter(blogReview.getMember().getNickname());
            simpleBlogDto.setViewCount(blogReview.getViewCount());
            return simpleBlogDto;
        }).collect(Collectors.toList());
        pagingDto paging = new pagingDto(pageBlogs.getNumber(), pageBlogs.getSize(), pageBlogs.getTotalElements(), pageBlogs.getTotalPages());
        dto.setBlogInfo(simpleBlogDtoList);
        dto.setPageInfo(paging);
        return dto;
    }
    //3. 블로그 목록 : 마이페이지 좋아요
    default BlogResponseDto.BlogListDto pageableLikesToMyBlogLikesDto(Page<BlogLikes> pageLikes){
        BlogResponseDto.BlogListDto dto = new BlogResponseDto.BlogListDto();
        List<BlogResponseDto.SimpleBlogDto> simpleBlogDtoList = pageLikes.get().map( blogLikes -> {
            BlogResponseDto.SimpleBlogDto simpleBlogDto = new BlogResponseDto.SimpleBlogDto();
            simpleBlogDto.setId(blogLikes.getBlog().getId());
            simpleBlogDto.setViewDate(blogLikes.getBlog().getViewDate());
            if(!CollectionUtils.isEmpty(blogLikes.getBlog().getImages()))
                simpleBlogDto.setImageInfo(imageListToImageDto(blogLikes.getBlog().getImages()));
                //simpleBlogDto.setImageInfo(imageToImageDto(blogLikes.getBlog().getImages().get(0))); //아직 이미지가 없는 건 처리못함
            simpleBlogDto.setTitle(blogLikes.getBlog().getTitle());
            simpleBlogDto.setWriter(blogLikes.getBlog().getMember().getNickname());
            simpleBlogDto.setViewCount(blogLikes.getBlog().getViewCount());
            return simpleBlogDto;
        }).collect(Collectors.toList());
        pagingDto paging = new pagingDto(pageLikes.getNumber(), pageLikes.getSize(),
                pageLikes.getTotalElements(), pageLikes.getTotalPages());
        dto.setBlogInfo(simpleBlogDtoList);
        dto.setPageInfo(paging);
        return dto;
    }
    //4. 블로그 목록 : 마이페이지 북마크
    default BlogResponseDto.BlogListDto pageableBookmarkToMyBlogBookmarkDto(Page<BlogBookmark> bookmarkPage){
        BlogResponseDto.BlogListDto dto = new BlogResponseDto.BlogListDto();
        List<BlogResponseDto.SimpleBlogDto> simpleBlogDtoList = bookmarkPage.get().map( blogBookmark -> {
            BlogResponseDto.SimpleBlogDto simpleBlogDto = new BlogResponseDto.SimpleBlogDto();
            simpleBlogDto.setId(blogBookmark.getBlog().getId());
            simpleBlogDto.setViewDate(blogBookmark.getBlog().getViewDate());
            if(!CollectionUtils.isEmpty(blogBookmark.getBlog().getImages()))
                simpleBlogDto.setImageInfo(imageListToImageDto(blogBookmark.getBlog().getImages()));
                //simpleBlogDto.setImageInfo(imageToImageDto(blogBookmark.getBlog().getImages().get(0))); //아직 이미지가 없는 건 처리못함
            simpleBlogDto.setTitle(blogBookmark.getBlog().getTitle());
            simpleBlogDto.setWriter(blogBookmark.getBlog().getMember().getNickname());
            simpleBlogDto.setViewCount(blogBookmark.getBlog().getViewCount());
            return simpleBlogDto;
        }).collect(Collectors.toList());
        pagingDto paging = new pagingDto(bookmarkPage.getNumber(), bookmarkPage.getSize(),
                bookmarkPage.getTotalElements(), bookmarkPage.getTotalPages());
        dto.setBlogInfo(simpleBlogDtoList);
        dto.setPageInfo(paging);
        return dto;
    }

    //** 그 외 **
    //1. 멤버 DTO
    BlogResponseDto.SimpleMemberDto memberToSimpleMemberDto(Member member);
    //2. 전시회 DTO
    BlogResponseDto.SimpleExhibitionDto exhibitionToSimpleExhibitionDto(Exhibition exhibition);
    //3. 이미지 하나 DTO
    ImageResponseDto imageToImageDto(BlogImage image);
    //4. 이미지 리스트 중 대표사진 DTO
    default ImageResponseDto imageListToImageDto(List<BlogImage> imageList){
        return imageToImageDto(imageList.get(0));
    }
    //5. 이미지 리스트 전체 조회 DTO
    default List<ImageResponseDto> imageToImageDtoList(List<BlogImage> images){
        List<ImageResponseDto> imageResponseDtoList = images.stream().map(i ->{
            ImageResponseDto dto = imageToImageDto(i);
            return dto;
        }).collect(Collectors.toList());
        return imageResponseDtoList;
    }
}
