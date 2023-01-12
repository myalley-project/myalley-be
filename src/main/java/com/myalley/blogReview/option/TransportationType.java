package com.myalley.blogReview.option;

public enum TransportationType {
    Bus("대중교통"),
    Walk("도보"),
    Car("자차");

    private String value;
    TransportationType(String value){
        this.value=value;
    }

    public String getKey() {
        return name();
    }

    public String getValue() {
        return value;
    }
}
