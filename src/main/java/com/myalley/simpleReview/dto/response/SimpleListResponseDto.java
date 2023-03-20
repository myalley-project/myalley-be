package com.myalley.simpleReview.dto.response;

import com.myalley.blogReview.dto.response.BlogListDto;
import com.myalley.blogReview.dto.response.BlogListResponseDto;
import com.myalley.common.dto.pagingDto;
import com.myalley.simpleReview.domain.SimpleReview;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class SimpleListResponseDto {
    private List<SimpleListDto> simpleInfo;
    private pagingDto pageInfo;

    public static SimpleListResponseDto of(Page<SimpleReview> simpleReviewPage, String type){
        SimpleListResponseDto listResponseDto = new SimpleListResponseDto();
        if(type.equals("member"))
            listResponseDto.setSimpleInfo(simpleReviewPage.get().map(SimpleListDto::memberFrom)
                    .collect(Collectors.toList()));
        else if(type.equals("exhibition"))
            listResponseDto.setSimpleInfo(simpleReviewPage.get().map(SimpleListDto::exhibitionFrom)
                    .collect(Collectors.toList()));
        listResponseDto.setPageInfo(new pagingDto(simpleReviewPage.getNumber()+1, simpleReviewPage.getSize(),
                simpleReviewPage.getTotalElements(), simpleReviewPage.getTotalPages()));
        return listResponseDto;
    }
}
