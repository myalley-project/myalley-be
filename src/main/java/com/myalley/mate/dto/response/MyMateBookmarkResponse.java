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
public class MyMateBookmarkResponse {
    private Long mateId;
    private String title;
    private String availableDate;
    private String status;
    private String mateGender;
    private String mateAge;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private Integer viewCount;
    private ExhibitionMateListResponse exhibition;

    public static MyMateBookmarkResponse of(MyMateBookmarkResponse mateBookmark) {
        return new MyMateBookmarkResponse(mateBookmark.mateId, mateBookmark.getTitle(), mateBookmark.getAvailableDate(),
                mateBookmark.getStatus(), mateBookmark.getMateGender(), mateBookmark.getMateAge(),
                mateBookmark.getCreatedAt(), mateBookmark.getViewCount(),
                new ExhibitionMateListResponse(mateBookmark.getExhibition().getExhibitionId(),mateBookmark.getExhibition().getExhibitionTitle(),
                        mateBookmark.getExhibition().getExhibitionSpace(), mateBookmark.getExhibition().getPosterUrl(),
                        mateBookmark.getExhibition().getExhibitionStatus()));
    }

    @QueryProjection
    public MyMateBookmarkResponse(Long mateId, String title, String status, String mateGender, String mateAge,
                                  String availableDate, Integer viewCount, LocalDateTime createdAt,
                                  Long exhibitionId, String exhibitionTitle,
                                  String posterUrl, String space, String exhibitionStatus) {
        this.mateId = mateId;
        this.title = title;
        this.status = status;
        this.mateGender = mateGender;
        this.mateAge = mateAge;
        this.availableDate = availableDate;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.exhibition = new ExhibitionMateListResponse(exhibitionId, exhibitionTitle,
                space, posterUrl, exhibitionStatus);
    }
}
