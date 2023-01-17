package com.myalley.mate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.exhibition.dto.response.ExhibitionResponse;
import com.myalley.mate.domain.Mate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateDetailResponse {

    private Long id;
    private String title;
    private String status;
    private String mateGender;
    private String mateAge;
    private String availableDate;
    private String content;
    private String contact;
    private Integer viewCount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    private ExhibitionResponse exhibition;

//private MemberResponse member; //추가 예정
//아래 항목 담길 예정
//    private Long memberId;
//    private String memberNickname;
//    private String memberProfileImg;
//    private String memberGender;
//    private String memberAge;

    public static MateDetailResponse of (Mate mate) {
        return new MateDetailResponse(mate.getId(), mate.getTitle(), mate.getStatus(), mate.getMateGender(),
                mate.getMateAge(), mate.getAvailableDate(), mate.getContent(), mate.getContact(),
                mate.getViewCount(), mate.getCreatedAt(), new ExhibitionResponse(mate.getExhibition().getId(),
                mate.getExhibition().getTitle(), mate.getExhibition().getSpace(),
                mate.getExhibition().getPosterUrl(), mate.getExhibition().getDate().substring(0,10),
                mate.getExhibition().getDate().substring(11,21), mate.getExhibition().getStatus().getValue()));
    }
}
