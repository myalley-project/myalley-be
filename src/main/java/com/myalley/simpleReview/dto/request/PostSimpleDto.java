package com.myalley.simpleReview.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class PostSimpleDto {
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
