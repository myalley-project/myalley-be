package com.myalley.mate.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.exhibition.dto.response.ExhibitionMateListResponse;
import com.querydsl.core.annotations.QueryProjection;
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

    public static MateSimpleResponse of(MateSimpleResponse mate) {
        return new MateSimpleResponse(mate.mateId, mate.getTitle(), mate.getAvailableDate(), mate.getStatus(),
                mate.getMateGender(), mate.getMateAge(), mate.getCreatedAt(), mate.getViewCount(),
                mate.getMemberId(), mate.getMemberNickname(),
                new ExhibitionMateListResponse(mate.getExhibition().getExhibitionId(), mate.getExhibition().getExhibitionTitle(),
                        mate.getExhibition().getExhibitionSpace(), mate.getExhibition().getPosterUrl(), mate.getExhibition().getExhibitionStatus()));
    }

    @QueryProjection
    public MateSimpleResponse(Long mateId, String title, String status, String mateGender, String mateAge,
                              String availableDate, Integer viewCount, LocalDateTime createdAt, Long exhibitionId,
                              String exhibitionTitle, String posterUrl, String space, String exhibitionStatus,
                              Long memberId, String memberNickname) {
        this.mateId = mateId;
        this.title = title;
        this.status = status;
        this.mateGender = mateGender;
        this.mateAge = mateAge;
        this.availableDate = availableDate;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.exhibition = new ExhibitionMateListResponse(exhibitionId, exhibitionTitle, space, posterUrl, exhibitionStatus);
        this.memberId = memberId;
        this.memberNickname = memberNickname;
    }
    
}
