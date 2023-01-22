package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.domain.ExhibitionBookmark;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionBasicResponse {

    private Long id;
    private String title;
    private String space;
    private String posterUrl;
    private String duration;
    private String type;
    private String status;
    private Integer viewCount;

    public static ExhibitionBasicResponse of(final Exhibition exhibition) {
        return new ExhibitionBasicResponse(exhibition.getId(), exhibition.getTitle(), exhibition.getSpace(),
                exhibition.getPosterUrl(), exhibition.getDuration(), exhibition.getType(),
                exhibition.getStatus(), exhibition.getViewCount());
    }

    public static ExhibitionBasicResponse of(ExhibitionBookmark exhibitionBookmark) {
        return new ExhibitionBasicResponse(exhibitionBookmark.getExhibition().getId(), exhibitionBookmark.getExhibition().getTitle(),
                exhibitionBookmark.getExhibition().getSpace(), exhibitionBookmark.getExhibition().getPosterUrl(),
                exhibitionBookmark.getExhibition().getDuration(), exhibitionBookmark.getExhibition().getType(),
                exhibitionBookmark.getExhibition().getStatus(), exhibitionBookmark.getExhibition().getViewCount());
    }
}
