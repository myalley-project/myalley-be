package com.myalley.blogReview.dto;

import com.myalley.blogReview.option.CongestionType;
import com.myalley.blogReview.option.TransportationType;
import com.myalley.blogReview.option.RevisitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@AllArgsConstructor
public class BlogRequestDto {
    private String viewDate;
    private String title;
    private String content;
    private MultipartFile[] images;
    private TransportationType transportation;  //이용한 교통수단
    private RevisitType revisit;  //재방문의향
    private CongestionType congestion; //혼잡도
}
