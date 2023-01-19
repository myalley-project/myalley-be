package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionMateListResponse {

    private Long exhibitionId;
    private String exhibitionTitle;
    private String exhibitionSpace;
    private String posterUrl;
    private String exhibitionStatus;

    public static ExhibitionMateListResponse of(Exhibition exhibition) {
        return new ExhibitionMateListResponse(exhibition.getId(), exhibition.getTitle(), exhibition.getSpace(),
                exhibition.getPosterUrl(), exhibition.getStatus());
    }
}
