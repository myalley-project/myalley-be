package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionResponse {

    private Long exhibitionId;
    private String exhibitionTitle;
    private String exhibitionSpace;
    private String posterUrl;
    private String exhibitionDuration;
    private String status;

    public static ExhibitionResponse of(Exhibition exhibition) {
        return new ExhibitionResponse(exhibition.getId(), exhibition.getTitle(), exhibition.getSpace(),
                exhibition.getPosterUrl(), exhibition.getDuration(), exhibition.getStatus());
    }
}