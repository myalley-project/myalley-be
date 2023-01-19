package com.myalley.exhibition.options;

import com.myalley.common.option.EnumMapperType;

public enum ExhibitionStatus implements EnumMapperType {
    BEFORE("지난 전시"),
    CURRENT("현재 전시"),
    FUTURE("예정 전시");

    public String value;

    ExhibitionStatus(String value) {
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
