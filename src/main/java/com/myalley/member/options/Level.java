package com.myalley.member.options;

import com.myalley.common.option.EnumMapperType;

public enum Level implements EnumMapperType {
    LEVEL1("level1"),
    LEVEL2("level2"),
    LEVEL3("level3"),
    LEVEL4("level4"),
    LEVEL5("level5");

    public String value;


    Level(String value){
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
