package com.myalley.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateUpdateRequest {
    private Long mateId;
    private String title;
    private String status;
    private String mateGender;
    private String mateAge;
    private String availableDate;
    private String content;
    private String contact;
    private Long exhibitionId;

    public MateUpdateRequest(String title, String status, String mateGender, String mateAge,
                String availableDate, String content, String contact, Integer viewCount) {
        this.title = title;
        this.status = status;
        this.mateGender = mateGender;
        this.mateAge = mateAge;
        this.availableDate = availableDate;
        this.content = content;
        this.contact = contact;
    }

    public void setId(Long id) {
        this.mateId = id;
    }
}
