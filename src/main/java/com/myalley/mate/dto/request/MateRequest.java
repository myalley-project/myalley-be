package com.myalley.mate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "모집 상태를 선택해주세요.")
    private String status;
    @NotBlank(message = "희망하는 관람 메이트 성별 옵션을 선택해주세요.")
    private String mateGender;
    @NotBlank(message = "희망하는 관람 메이트의 연령 옵션을 선택해주세요.")
    private String mateAge;
    @NotBlank(message = "희망하는 관람일 옵션을 선택해주세요.")
    private String availableDate;
    @NotBlank(message = "메이트 모집글 본문 내용을 작성해주세요.")
    private String content;

    private String contact;
    @NotNull(message = "관람을 희망하는 전시회의 id를 포함해야 합니다.")
    @Positive(message = "전시회 id는 0이상의 양수입니다.")
    private Long exhibitionId;
}
