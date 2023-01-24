package com.myalley.simpleReview.mapper;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.member.domain.Member;
import com.myalley.simpleReview.domain.SimpleReview;
import com.myalley.simpleReview.dto.SimpleRequestDto;
import com.myalley.simpleReview.dto.SimpleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SimpleReviewMapper {
    SimpleReviewMapper INSTANCE = Mappers.getMapper(SimpleReviewMapper.class);

    SimpleReview postSimpleDtoToSimpleReview(SimpleRequestDto.PostSimpleDto requestDto);
    SimpleReview patchSimpleDtoToSimpleReview(SimpleRequestDto.PatchSimpleDto requestDto);

    SimpleResponseDto.SimpleExhibitionResponseDto simpleExhibitionDtoToExhibition(Exhibition exhibition);
    SimpleResponseDto.SimpleMemberResponseDto simpleMemberDtoToMember(Member member);

    default SimpleResponseDto.GetSimpleResponseDto simpleReviewToGetSimpleResponseDto(SimpleReview simpleReview){
        SimpleResponseDto.GetSimpleResponseDto responseDto = new SimpleResponseDto.GetSimpleResponseDto();
        responseDto.setId(simpleReview.getId());
        responseDto.setViewDate(simpleReview.getViewDate());
        responseDto.setRate(simpleReview.getRate());
        responseDto.setContent(simpleReview.getContent());
        responseDto.setCongestion(simpleReview.getCongestion());
        responseDto.setTime(simpleReview.getTime());
        responseDto.setMemberInfo(simpleMemberDtoToMember(simpleReview.getMember()));
        responseDto.setExhibitionInfo(simpleExhibitionDtoToExhibition(simpleReview.getExhibition()));
        return responseDto;
    }

}
