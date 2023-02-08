package com.myalley.mate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.exhibition.dto.response.ExhibitionMateListResponse;
import com.myalley.mate.domain.Mate;
import com.myalley.mate.domain.MateBookmark;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MateSimpleResponse {

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
    private ExhibitionMateListResponse exhibition;

    public static MateSimpleResponse of(Mate mate) {
        return new MateSimpleResponse(mate.getId(), mate.getTitle(), mate.getAvailableDate(), mate.getStatus(),
                mate.getMateGender(), mate.getMateAge(), mate.getCreatedAt(), mate.getViewCount(),
                mate.getMember().getMemberId(), mate.getMember().getNickname(),
                new ExhibitionMateListResponse(mate.getExhibition().getId(), mate.getExhibition().getTitle(),
                        mate.getExhibition().getSpace(), mate.getExhibition().getPosterUrl(), mate.getExhibition().getStatus()));
    }

    public static MateSimpleResponse of(MateBookmark mateBookmark) {
        return new MateSimpleResponse(mateBookmark.getMate().getId(), mateBookmark.getMate().getTitle(), mateBookmark.getMate().getAvailableDate(),
                mateBookmark.getMate().getStatus(), mateBookmark.getMate().getMateGender(), mateBookmark.getMate().getMateAge(),
                mateBookmark.getMate().getCreatedAt(), mateBookmark.getMate().getViewCount(), mateBookmark.getMate().getMember().getMemberId(),
                mateBookmark.getMate().getMember().getNickname(),
                new ExhibitionMateListResponse(mateBookmark.getMate().getExhibition().getId(),mateBookmark.getMate().getExhibition().getTitle(),
                        mateBookmark.getMate().getExhibition().getSpace(), mateBookmark.getMate().getExhibition().getPosterUrl(),
                        mateBookmark.getMate().getExhibition().getStatus()));
    }
}
