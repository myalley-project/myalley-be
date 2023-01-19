package com.myalley.blogReview.mapper;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.dto.BlogResponseDto;
import com.myalley.blogReview.dto.ImageResponseDto;
import com.myalley.test_user.TestMember;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;


@Mapper
public interface BlogReviewMapper {
    BlogReviewMapper INSTANCE = Mappers.getMapper(BlogReviewMapper.class);

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
        dto.setViewCount(blog.getViewCount());
        dto.setImages(imageToImageDtoList(blog.getImages()));
        dto.setMemberInfo(memberToSimpleMemberDto(blog.getTestMember()));
        dto.setExhibitionId(blog.getExhibition());

        return dto;
    }
    BlogResponseDto.SimpleMemberDto memberToSimpleMemberDto(TestMember testMember);
    default BlogResponseDto.SimpleBlogDto blogToSimpleBlogDto(BlogReview blog){
        BlogResponseDto.SimpleBlogDto dto = new BlogResponseDto.SimpleBlogDto();
        dto.setId(blog.getId());
        dto.setTitle(blog.getTitle());
        dto.setWriter(blog.getTestMember().getNickname());
        dto.setViewDate(blog.getViewDate());
        dto.setViewCount(blog.getViewCount());
        dto.setImageInfo(imageListToImageDto(blog.getImages()));
        return dto;
    }

    ImageResponseDto imageToImageDto(BlogImage image);

    default ImageResponseDto imageListToImageDto(List<BlogImage> imageList){
        return imageToImageDto(imageList.get(0));
    }

    default List<ImageResponseDto> imageToImageDtoList(List<BlogImage> images){
        List<ImageResponseDto> imageResponseDtoList = images.stream().map(i ->{
            ImageResponseDto dto = imageToImageDto(i);
            return dto;
        }).collect(Collectors.toList());
        return imageResponseDtoList;
    }


}
