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
    private Long exhibitionId;
    private String viewDate;
    private String title;
    private String content;
    private MultipartFile[] images; //확실하지 않음. 아직 Json으로 받아오는 거 하지 않음. 적용안함
    private TransportationType transportation;
    private RevisitType revisit;
    private CongestionType congestion;
}
