package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionDetailResponse {

    private Long id;
    private String title;
    private String adultPrice;
    private String space;
    private String posterUrl;
    private String startDate;
    private String endDate;
    private String webLink;
    private String content;
    private String author;
    private Integer viewCount;
    private String status;
    private String type;

    public static ExhibitionDetailResponse of (Exhibition exhibition) {
        return new ExhibitionDetailResponse(exhibition.getId(), exhibition.getTitle(), exhibition.getAdultPrice(),
                exhibition.getSpace(), exhibition.getPosterUrl(), exhibition.getDate().substring(0,10),
                exhibition.getDate().substring(11,21), exhibition.getWebLink(), exhibition.getContent(),
                exhibition.getAuthor(), exhibition.getViewCount(), exhibition.getStatus().getValue(),
                exhibition.getType().getValue());
    }
}
