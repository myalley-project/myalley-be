package com.myalley.member.options;

import com.myalley.common.option.EnumMapperType;

public enum Authority implements EnumMapperType {

    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");


    public String value;

    Authority(String value){
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


