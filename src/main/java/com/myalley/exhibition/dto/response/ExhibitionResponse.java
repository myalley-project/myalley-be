package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionResponse {

    private Long exhibitionId;
    private String space;
    private String posterUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer viewCount;

    public static ExhibitionResponse of(Exhibition exhibition) {
        return new ExhibitionResponse(exhibition.getId(), exhibition.getSpace(),
                exhibition.getPosterUrl(), exhibition.getStartDate(), exhibition.getEndDate(), exhibition.getViewCount());
    }
}
