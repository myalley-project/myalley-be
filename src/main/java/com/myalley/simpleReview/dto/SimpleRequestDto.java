package com.myalley.simpleReview.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
        @Size(min=10,max=60)
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
        @Size(min=10,max=60)
        private String content;
        private String time;
        private String congestion;
    }
}
