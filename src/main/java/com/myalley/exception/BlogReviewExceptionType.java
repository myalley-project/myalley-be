package com.myalley.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BlogReviewExceptionType implements BaseExceptionType{
    BLOG_NOT_FOUND(404,"요청에 맞는 블로그 리뷰가 존재하지 않습니다."),
    BLOG_FORBIDDEN(403,"작성자만 이용할 수 있습니다."),
    BLOG_CONFLICT(409,"이미 해당 전시회의 블로그 리뷰 글을 작성하였습니다."),
    IMAGE_BAD_REQUEST(400,"올바른 형식의 이미지 파일이 아닙니다."),
    IMAGE_NOT_FOUND(404,"요청에 맞는 이미지를 찾을 수 없습니다.");


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
