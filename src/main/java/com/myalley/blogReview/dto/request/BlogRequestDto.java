package com.myalley.blogReview.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class BlogRequestDto {
    //post랑 put이 다른 점이 없어서 하나로 같이 받아도 될 것 같음
    @NotNull(message = "관람일은 필수로 입력해야합니다.")
    private String viewDate;
    @NotBlank(message = "블로그 제목은 필수로 입력해야합니다.")
    @Size(max=255, message = "블로그 제목은 최대 255자까지 입력할 수 있습니다.")
    private String title;
    @NotBlank(message = "블로그 내용은 필수로 입력해야합니다.")
    @Size(max=3000, message = "블로그 내용은 최대 3000자까지 입력할 수 있습니다.")
    private String content;
    @NotNull(message = "관람 시간은 필수로 입력해야합니다.")
    private String time;
    private String transportation;
    private String revisit;
    private String congestion;
}
