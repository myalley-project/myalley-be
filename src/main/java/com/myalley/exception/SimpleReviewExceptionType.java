package com.myalley.exception;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum SimpleReviewExceptionType implements BaseExceptionType{
    SIMPLE_BAD_REQUEST(400,"올바른 형식의 요청이 아닙니다."), //임시
    SIMPLE_NOT_FOUND(404,"요청에 맞는 한 줄 리뷰가 존재하지 않습니다."),
    SIMPLE_FORBIDDEN(403,"작성자만 이용할 수 있습니다.");


    private int errorCode;
    private String errorMsg;

    @Override
    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
