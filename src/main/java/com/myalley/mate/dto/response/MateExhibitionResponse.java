package com.myalley.mate.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.exhibition.dto.response.ExhibitionSimpleResponse;
import com.myalley.mate.domain.Mate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateExhibitionResponse {
    private Long mateId;
    private String title;
    private String availableDate;
    private String status;
    private String mateGender;
    private String mateAge;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Integer viewCount;
    private Long memberId;
    private String memberNickname;
    private ExhibitionSimpleResponse exhibition;

    public static MateExhibitionResponse of(Mate mate) {
        return new MateExhibitionResponse(mate.getId(), mate.getTitle(), mate.getAvailableDate(), mate.getStatus(),
                mate.getMateGender(), mate.getMateAge(), mate.getCreatedAt(), mate.getViewCount(),
                mate.getMember().getMemberId(), mate.getMember().getNickname(),
                new ExhibitionSimpleResponse(mate.getExhibition().getId(), mate.getExhibition().getTitle(),
                        mate.getExhibition().getSpace(), mate.getExhibition().getStatus()));
    }
}
