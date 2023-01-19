package com.myalley.blogReview.option;

public enum RevisitType {
    Never("전혀 없음"), //숫자로 입력할까?
    Maybe("가능성 있음"),
    Must("완전 있음");

    private String value;
    RevisitType(String value){
        this.value=value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
