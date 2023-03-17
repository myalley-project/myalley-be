package com.myalley.simpleReview.mapper;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.domain.SimpleReview.SimpleReviewBuilder;
import com.myalley.simpleReview.dto.SimpleRequestDto.PatchSimpleDto;
import com.myalley.simpleReview.dto.SimpleResponseDto.SimpleExhibitionResponseDto;
import com.myalley.simpleReview.dto.SimpleResponseDto.SimpleMemberResponseDto;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-03-18T01:37:26+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.15 (Oracle Corporation)"
)
public class SimpleReviewMapperImpl implements SimpleReviewMapper {

    @Override
    public SimpleReview putSimpleDtoToSimpleReview(PatchSimpleDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        SimpleReviewBuilder simpleReview = SimpleReview.builder();

        simpleReview.viewDate( requestDto.getViewDate() );
        simpleReview.time( requestDto.getTime() );
        simpleReview.congestion( requestDto.getCongestion() );
        simpleReview.rate( requestDto.getRate() );
        simpleReview.content( requestDto.getContent() );

        return simpleReview.build();
    }

    @Override
    public SimpleExhibitionResponseDto simpleExhibitionDtoToExhibition(Exhibition exhibition) {
        if ( exhibition == null ) {
            return null;
        }

        SimpleExhibitionResponseDto simpleExhibitionResponseDto = new SimpleExhibitionResponseDto();

        simpleExhibitionResponseDto.setId( exhibition.getId() );
        simpleExhibitionResponseDto.setTitle( exhibition.getTitle() );

        return simpleExhibitionResponseDto;
    }

    @Override
    public SimpleMemberResponseDto simpleMemberDtoToMember(Member member) {
        if ( member == null ) {
            return null;
        }

        SimpleMemberResponseDto simpleMemberResponseDto = new SimpleMemberResponseDto();

        simpleMemberResponseDto.setMemberId( member.getMemberId() );
        simpleMemberResponseDto.setNickname( member.getNickname() );
        simpleMemberResponseDto.setMemberImage( member.getMemberImage() );

        return simpleMemberResponseDto;
    }
}
