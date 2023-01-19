package com.myalley.exhibition.options;

import com.myalley.common.option.EnumMapperType;

public enum ExhibitionType implements EnumMapperType {
    ARCHITECTURE("건축 전시"),
    THEATER("공연예술 전시"),
    PAINTING("그림 전시"),
    PLANNED("기획 전시"),
    SCULPTURE("조각 전시"),
    LITERATURE("문학 전시"),
    PICTURE("사진 전시"),
    COLLECTION("소장품 전시"),
    ART_FAIR("아트페어"),
    HISTORY("역사 전시"),
    VIDEO("영상 전시"),
    CINEMA("영화 전시"),
    MUSIC("음악 전시"),
    TEMPORARY("임시 전시"),
    OPEN_SUBMISSION("자유출품 전시"),
    SITE_SPECIFIC("장소특정적 전시"),
    MONOGRAPHIC("전문 전시"),
    PERIODIC("정기 전시"),
    SURVEY("조망 전시"),
    IN_FOCUS("초점 전시"),
    COMMISSION("커미션 전시"),
    SPECIAL("특별 전시"),
    ETC("기타");

    public String value;

    ExhibitionType(String value) {
        this.value = value;
    }

    @Override
    public String getKey() {
        return name();
    }

    @Override
    public String getValue() {
        return value;
    }
}
