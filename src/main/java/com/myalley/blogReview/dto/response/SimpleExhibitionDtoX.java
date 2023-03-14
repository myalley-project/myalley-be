package com.myalley.blogReview.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleExhibitionDtoX {
    private Long id;
    private String title;
    private String posterUrl;
    private String duration;
    private String space;
    private String type;

    public static SimpleExhibitionDtoX from(Exhibition exhibition){
        return new SimpleExhibitionDtoX(exhibition.getId(), exhibition.getTitle(), exhibition.getPosterUrl(),
                exhibition.getDuration(), exhibition.getSpace(), exhibition.getType());
    }
}
