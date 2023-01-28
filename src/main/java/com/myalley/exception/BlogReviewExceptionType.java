package com.myalley.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BlogReviewExceptionType implements BaseExceptionType{
    BLOG_NOT_FOUND(404,"요청에 맞는 블로그 리뷰가 존재하지 않습니다."),
    BLOG_FORBIDDEN(403,"작성자만 이용할 수 있습니다."),
    LIKES_BAD_REQUEST(400,"올바르지 않은 요청입니다."),
    LIKES_FORBIDDEN(403,"본인의 글을 좋아요 할 수 없습니다."),
    BOOKMARK_BAD_REQUEST(400,"올바르지 않은 요청입니다."),
    BOOKMARK_FORBIDDEN(403,"본인의 글을 북마크 할 수 없습니다."),
    IMAGE_BAD_REQUEST_EMPTY(400,"이미지 파일을 첨부해야 합니다."),
    IMAGE_BAD_REQUEST_OVER(400,"이미지 파일 첨부 최대 개수는 3장입니다."),
    IMAGE_BAD_REQUEST_INCORRECT(400,"올바른 형식의 이미지 파일이 아닙니다."),
    IMAGE_FORBIDDEN(403,"블로그 작성자만 이용할 수 있습니다."),
    IMAGE_NOT_FOUND(404,"요청에 맞는 이미지를 찾을 수 없습니다");


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
