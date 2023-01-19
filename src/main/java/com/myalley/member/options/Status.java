package com.myalley.member.options;

import com.myalley.EnumMapperType;

public enum Status implements EnumMapperType {
    활동중("activity"),
    휴면("stop");

    public String value;


    Status(String value){
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
