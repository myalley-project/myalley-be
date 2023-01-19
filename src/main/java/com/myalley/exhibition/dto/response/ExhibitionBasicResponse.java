package com.myalley.exhibition.dto.response;

import com.myalley.exhibition.domain.Exhibition;
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

//    @Builder
    public ExhibitionBasicResponse(Exhibition exhibition) {
        this.id = exhibition.getId();
        this.title = exhibition.getTitle();
        this.space = exhibition.getSpace();
        this.posterUrl = exhibition.getPosterUrl();
        this.duration = exhibition.getDuration();
        this.type = exhibition.getType();
        this.status = exhibition.getStatus();
        this.viewCount = exhibition.getViewCount();
    }


    public static ExhibitionBasicResponse of(final Exhibition exhibition) {
        return new ExhibitionBasicResponse(exhibition.getId(), exhibition.getTitle(), exhibition.getSpace(),
                exhibition.getPosterUrl(), exhibition.getDuration(), exhibition.getType(),
                exhibition.getStatus(), exhibition.getViewCount());
    }
}
