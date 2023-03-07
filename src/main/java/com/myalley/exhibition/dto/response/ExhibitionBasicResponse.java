package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import com.myalley.exhibition.domain.ExhibitionBookmark;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ExhibitionBasicResponse {

    private Long id;
    private String title;
    private String space;
    private String posterUrl;
    private String duration;
    private String type;
    private String status;
    private Integer viewCount;

    @Builder
    public ExhibitionBasicResponse(Long id, String title, String space, String posterUrl, String duration, String type, String status, Integer viewCount) {
        this.id = id;
        this.title = title;
        this.space = space;
        this.posterUrl = posterUrl;
        this.duration = duration;
        this.type = type;
        this.status = status;
        this.viewCount = viewCount;
    }


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
