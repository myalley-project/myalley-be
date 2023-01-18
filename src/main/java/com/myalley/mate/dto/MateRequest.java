package com.myalley.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateRequest {

    private String title;
    private String status;
    private String mateGender;
    private String mateAge;
    private String availableDate;
    private String content;
    private String contact;
    private Long exhibitionId;
}
