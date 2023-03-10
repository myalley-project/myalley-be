package com.myalley.member.options;

import com.myalley.common.option.EnumMapperType;

public enum BlackStatus implements EnumMapperType {
    BLACK("black"),
    NORMAL("normal");

    public String value;


    BlackStatus(String value){
        this.value=value;
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
