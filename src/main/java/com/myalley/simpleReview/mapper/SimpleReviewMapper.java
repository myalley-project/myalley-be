package com.myalley.simpleReview.mapper;

import com.myalley.common.dto.pagingDto;
import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.dto.SimpleRequestDto;
import com.myalley.simpleReview.dto.SimpleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.stream.Collectors;

@Mapper
public interface SimpleReviewMapper {
    SimpleReviewMapper INSTANCE = Mappers.getMapper(SimpleReviewMapper.class);

    default SimpleReview postSimpleDtoToSimpleReview(SimpleRequestDto.PostSimpleDto requestDto){
        SimpleReview.SimpleReviewBuilder simpleReview = SimpleReview.builder();

        simpleReview.viewDate( requestDto.getViewDate() );
        simpleReview.rate( requestDto.getRate() );
        simpleReview.content( requestDto.getContent() );
        if(requestDto.getCongestion() == null)
            simpleReview.congestion( "모르겠음" );
        else
            simpleReview.congestion( requestDto.getCongestion() );
        if(requestDto.getTime() == null)
            simpleReview.time( "모르겠음" );
        else
            simpleReview.time( requestDto.getTime() );

        return simpleReview.build();
    }

    SimpleReview putSimpleDtoToSimpleReview(SimpleRequestDto.PatchSimpleDto requestDto);

    SimpleResponseDto.SimpleExhibitionResponseDto simpleExhibitionDtoToExhibition(Exhibition exhibition);
    SimpleResponseDto.SimpleMemberResponseDto simpleMemberDtoToMember(Member member);

    default SimpleResponseDto.ListExhibitionSimpleResponseDto listExhibitionSimpleReviewDto(Page<SimpleReview> simpleReviewPage){
        SimpleResponseDto.ListExhibitionSimpleResponseDto responseDto
                = new SimpleResponseDto.ListExhibitionSimpleResponseDto();
        responseDto.setSimpleInfo(simpleReviewPage.get()
                .map( s -> {
                    SimpleResponseDto.ExhibitionSimpleReviewResponseDto oneDto
                            = new SimpleResponseDto.ExhibitionSimpleReviewResponseDto();
                    oneDto.setId(s.getId());
                    oneDto.setRate(s.getRate());
                    oneDto.setViewDate(s.getViewDate());
                    oneDto.setContent(s.getContent());
                    oneDto.setTime(s.getTime());
                    oneDto.setCongestion(s.getCongestion());
                    oneDto.setMemberInfo(simpleMemberDtoToMember(s.getMember()));
                    return oneDto;
                }).collect(Collectors.toList()));
        responseDto.setPageInfo(new pagingDto(simpleReviewPage.getNumber()+1,simpleReviewPage.getSize(),
                simpleReviewPage.getTotalElements(),simpleReviewPage.getTotalPages()));
        return responseDto;
    }

    default SimpleResponseDto.ListUserSimpleResponseDto listUserSimpleReviewDto(Page<SimpleReview> simpleReviewPage){
        SimpleResponseDto.ListUserSimpleResponseDto responseDto
                = new SimpleResponseDto.ListUserSimpleResponseDto();
        responseDto.setSimpleInfo(simpleReviewPage.get()
                .map( s -> {
                    SimpleResponseDto.UserSimpleReviewResponseDto oneDto
                            = new SimpleResponseDto.UserSimpleReviewResponseDto();
                    oneDto.setId(s.getId());
                    oneDto.setRate(s.getRate());
                    oneDto.setTime(s.getTime());
                    oneDto.setViewDate(s.getViewDate());
                    oneDto.setCongestion(s.getCongestion());
                    oneDto.setContent(s.getContent());
                    oneDto.setExhibitionInfo(simpleExhibitionDtoToExhibition(s.getExhibition()));
                    return oneDto;
                }).collect(Collectors.toList()));
        responseDto.setPageInfo(new pagingDto(simpleReviewPage.getNumber()+1,simpleReviewPage.getSize(),
                simpleReviewPage.getTotalElements(),simpleReviewPage.getTotalPages()));
        return responseDto;
    }

}
