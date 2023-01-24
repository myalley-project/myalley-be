package com.myalley.simpleReview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SimpleRequestDto {

    @Data
    @NoArgsConstructor
    public static class PostSimpleDto {
        @NotNull
        private Long exhibitionId;
        @NotNull
        private LocalDate viewDate;
        @NotNull
        private Integer rate;
        @NotNull
        private String content;
        private String time;
        private String congestion;
    }

    @Data
    @NoArgsConstructor
    public static class PatchSimpleDto {
        @NotNull
        private LocalDate viewDate;
        @NotNull
        private Integer rate;
        @NotNull
        private String content;
        private String time;
        private String congestion;
    }
}
