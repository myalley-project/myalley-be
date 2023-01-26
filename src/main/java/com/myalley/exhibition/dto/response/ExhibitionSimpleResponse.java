package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionSimpleResponse {
    private Long exhibitionId;
    private String exhibitionTitle;
    private String exhibitionSpace;
    private String exhibitionStatus;

    public static ExhibitionSimpleResponse of(Exhibition exhibition) {
        return new ExhibitionSimpleResponse(exhibition.getId(), exhibition.getTitle(),
                exhibition.getSpace(), exhibition.getStatus());
    }
}
