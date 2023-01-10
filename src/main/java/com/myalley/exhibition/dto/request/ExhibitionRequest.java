package com.myalley.exhibition.dto.request;

import com.myalley.exhibition.options.DeletionStatus;
import com.myalley.exhibition.options.ExhibitionStatus;
import com.myalley.exhibition.options.ExhibitionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionRequest {

    private String title;
    private ExhibitionStatus status;
    private ExhibitionType type;
    private String space;
    private String adultPrice;
    private String youthPrice;
    private String kidPrice;
    private String fileName;
    private String posterUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private String webLink;
    private String purpose;
    private String content;
    private String author;

}
