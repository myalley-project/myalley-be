package com.myalley.exhibition.options;

import com.myalley.common.option.EnumMapperType;

public enum DeletionStatus implements EnumMapperType {
    DELETED("Y"),
    NOT_DELETED("N");

    public String value;

    DeletionStatus(String value) {
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
