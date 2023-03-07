package com.myalley.mate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.myalley.exhibition.dto.response.ExhibitionResponse;
import com.myalley.mate.domain.Mate;
import com.myalley.member.dto.MemberMateDto;
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
    private Integer bookmarkCount;
    private boolean bookmarked;

    private ExhibitionResponse exhibition;

    private MemberMateDto member;

    public static MateDetailResponse of (Mate mate, boolean bookmarked) {
        return new MateDetailResponse(mate.getId(), mate.getTitle(), mate.getStatus(), mate.getMateGender(),
                mate.getMateAge(), mate.getAvailableDate(), mate.getContent(), mate.getContact(),
                mate.getViewCount(), mate.getCreatedAt(), mate.getBookmarkCount(), bookmarked,
                new ExhibitionResponse(mate.getExhibition().getId(),
                mate.getExhibition().getTitle(), mate.getExhibition().getSpace(),
                mate.getExhibition().getPosterUrl(), mate.getExhibition().getDuration(), mate.getExhibition().getStatus(), mate.getExhibition().getType()),
                new MemberMateDto(mate.getMember().getMemberId(), mate.getMember().getNickname(),
                        mate.getMember().getMemberImage(), mate.getMember().getGender().getKey(),
                        mate.getMember().getBirth().toString().substring(0,4)));
    }

    public static MateDetailResponse of (Mate mate) {
        return new MateDetailResponse(mate.getId(), mate.getTitle(), mate.getStatus(), mate.getMateGender(),
                mate.getMateAge(), mate.getAvailableDate(), mate.getContent(), mate.getContact(),
                mate.getViewCount(), mate.getCreatedAt(), mate.getBookmarkCount(), false,
                new ExhibitionResponse(mate.getExhibition().getId(),
                mate.getExhibition().getTitle(), mate.getExhibition().getSpace(),
                mate.getExhibition().getPosterUrl(), mate.getExhibition().getDuration(), mate.getExhibition().getStatus(), mate.getExhibition().getType()),
                new MemberMateDto(mate.getMember().getMemberId(), mate.getMember().getNickname(),
                        mate.getMember().getMemberImage(), mate.getMember().getGender().getKey(),
                        mate.getMember().getBirth().toString().substring(0,4)));
    }
}
