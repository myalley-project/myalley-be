package com.myalley.blogReview.option;

public enum CongestionType {
    Quiet("한산함"),
    Normal("보통"),
    Bustle("북적거림"),
    Waiting("대기함");

    private String value;

    CongestionType(String value){
        this.value=value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
