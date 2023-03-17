package com.myalley.blogReview.mapper;

import com.myalley.blogReview.domain.BlogImage;
import com.myalley.blogReview.domain.BlogReview;
import com.myalley.blogReview.domain.BlogReview.BlogReviewBuilder;
import com.myalley.blogReview.dto.BlogRequestDto.PutBlogDto;
import com.myalley.blogReview.dto.BlogResponseDto.SimpleExhibitionDto;
import com.myalley.blogReview.dto.BlogResponseDto.SimpleMemberDto;
import com.myalley.blogReview.dto.ImageResponseDto;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import java.time.LocalDate;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T01:37:26+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Oracle Corporation)"
)
public class BlogReviewMapperImpl implements BlogReviewMapper {

    @Override
    public BlogReview putBlogDtoToBlogReview(PutBlogDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        BlogReviewBuilder blogReview = BlogReview.builder();

        blogReview.title( requestDto.getTitle() );
        blogReview.content( requestDto.getContent() );
        if ( requestDto.getViewDate() != null ) {
            blogReview.viewDate( LocalDate.parse( requestDto.getViewDate() ) );
        }
        blogReview.time( requestDto.getTime() );
        blogReview.transportation( requestDto.getTransportation() );
        blogReview.revisit( requestDto.getRevisit() );
        blogReview.congestion( requestDto.getCongestion() );

        return blogReview.build();
    }

    @Override
    public SimpleMemberDto memberToSimpleMemberDto(Member member) {
        if ( member == null ) {
            return null;
        }

        SimpleMemberDto simpleMemberDto = new SimpleMemberDto();

        simpleMemberDto.setMemberId( member.getMemberId() );
        simpleMemberDto.setNickname( member.getNickname() );
        simpleMemberDto.setMemberImage( member.getMemberImage() );

        return simpleMemberDto;
    }

    @Override
    public SimpleExhibitionDto exhibitionToSimpleExhibitionDto(Exhibition exhibition) {
        if ( exhibition == null ) {
            return null;
        }

        SimpleExhibitionDto simpleExhibitionDto = new SimpleExhibitionDto();

        simpleExhibitionDto.setId( exhibition.getId() );
        simpleExhibitionDto.setTitle( exhibition.getTitle() );
        simpleExhibitionDto.setPosterUrl( exhibition.getPosterUrl() );
        simpleExhibitionDto.setDuration( exhibition.getDuration() );
        simpleExhibitionDto.setSpace( exhibition.getSpace() );
        simpleExhibitionDto.setType( exhibition.getType() );

        return simpleExhibitionDto;
    }

    @Override
    public ImageResponseDto imageToImageDto(BlogImage image) {
        if ( image == null ) {
            return null;
        }

        ImageResponseDto imageResponseDto = new ImageResponseDto();

        imageResponseDto.setId( image.getId() );
        imageResponseDto.setUrl( image.getUrl() );

        return imageResponseDto;
    }
}
