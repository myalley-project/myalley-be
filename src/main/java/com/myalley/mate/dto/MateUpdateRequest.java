package com.myalley.mate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateUpdateRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "모집 상태를 선택해주세요.")
    private String status;
    @NotBlank(message = "희망하는 관람 메이트의 성별을 선택해주세요.")
    private String mateGender;
    @NotBlank(message = "희망하는 관람 메이트의 연령을 선택해주세요.")
    private String mateAge;
    @NotBlank(message = "희망하는 관람일을 선택해주세요.")
    private String availableDate;
    @NotBlank(message = "메이트 모집글 본문 내용을 작성해주세요.")
    private String content;
    private String contact;

    @NotNull(message = "하나의 전시회를 반드시 선택해야 합니다.")
    @Positive(message = "희망하는 전시회를 선택해주세요.")
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
}
