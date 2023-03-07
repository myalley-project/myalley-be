package com.myalley.exhibition.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;


@Getter
@NoArgsConstructor
public class ExhibitionUpdateRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "전시회 관람 가능 여부를 선택해주세요.")
    private String status;
    @NotBlank(message = "전시회 유형을 선택해주세요.")
    private String type;
    @NotBlank(message = "전시회 장소를 입력해주세요.")
    private String space;
    @PositiveOrZero(message = "관람 비용은 0 이상의 숫자만 입력해주세요.")
    private Integer adultPrice;
    @NotBlank(message = "포스터 이미지 파일명을 입력해주세요.")
    private String fileName;
    @NotBlank(message = "포스터 이미지 Url을 입력해주세요.")
    private String posterUrl;
    @NotBlank(message = "전시회 기간을 입력해주세요.")
    private String duration;
    @NotBlank(message = "전시회 출처 웹사이트 링크를 입력해주세요.")
    @Pattern(regexp = "(https?:\\/\\/)?(www\\.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)|(https?:\\/\\/)?(www\\.)?(?!ww)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,4}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)", message = "올바른 웹사이트 주소를 입력해주세요.")
    private String webLink;
    @NotBlank(message = "전시회 설명을 작성해주세요.")
    private String content;
    private String author;

    public ExhibitionUpdateRequest( String title, String status, String type,
                                    String space, Integer adultPrice,
                                    String fileName, String posterUrl, String duration,
                                    String webLink, String content, String author) {
        this.title = title;
        this.status = status;
        this.type = type;
        this.space = space;
        this.adultPrice = adultPrice;
        this.fileName = fileName;
        this.posterUrl = posterUrl;
        this.duration = duration;
        this.webLink = webLink;
        this.content = content;
        this.author = author;
    }

}
