package com.myalley.simpleReview.dto.response;

import com.myalley.exhibition.domain.Exhibition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleExhibitionDto {
    private Long id;
    private String title;

    public static SimpleExhibitionDto from(Exhibition exhibition){
        return new SimpleExhibitionDto(exhibition.getId(), exhibition.getTitle());
    }
}
