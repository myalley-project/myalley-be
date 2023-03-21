package com.myalley.simpleReview.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.blogReview.dto.response.SimpleMemberDto;
import com.myalley.simpleReview.domain.SimpleReview;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleListDto {
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate viewDate;
    private Integer rate;
    private String content;
    private String time;
    private String congestion;
    private SimpleMemberDto memberInfo;
    private SimpleExhibitionDto exhibitionInfo;

    public static SimpleListDto memberFrom(SimpleReview simpleReview){
        return new SimpleListDto(simpleReview.getId(), simpleReview.getViewDate(), simpleReview.getRate(),
                simpleReview.getContent(), simpleReview.getTime(), simpleReview.getCongestion(),
                SimpleMemberDto.from(simpleReview.getMember()), null);
    }
    public static SimpleListDto exhibitionFrom(SimpleReview simpleReview){
        return new SimpleListDto(simpleReview.getId(), simpleReview.getViewDate(), simpleReview.getRate(),
                simpleReview.getContent(), simpleReview.getTime(), simpleReview.getCongestion(),
                null, SimpleExhibitionDto.from(simpleReview.getExhibition()));
    }
}
