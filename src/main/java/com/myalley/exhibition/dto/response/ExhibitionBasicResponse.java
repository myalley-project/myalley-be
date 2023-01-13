package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionBasicResponse {

    private Long exhibitionId;
    private String title;
    private String space;
    private String posterUrl;
    private String startDate;
    private String endDate;
    private Integer viewCount;
    private String status;

    public static ExhibitionBasicResponse of(Exhibition exhibition) {
        return new ExhibitionBasicResponse(exhibition.getId(), exhibition.getTitle(), exhibition.getSpace(),
                exhibition.getPosterUrl(), exhibition.getDate().substring(0,10),
                exhibition.getDate().substring(11,21), exhibition.getViewCount(), exhibition.getStatus().getValue());
    }
}
