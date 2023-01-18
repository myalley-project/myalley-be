package com.myalley.mate.dto;

import com.myalley.mate.domain.Mate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateSimpleResponse {

    private Long mateId;
    private String title;
    private String status;
    private String mateGender;
    private String mateAge;
    private String availableDate;

    public static MateSimpleResponse of(Mate mate) {
        return new MateSimpleResponse(mate.getId(), mate.getTitle(), mate.getStatus(), mate.getMateGender(),
                mate.getMateAge(), mate.getAvailableDate());
    }
}
