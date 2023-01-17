package com.myalley.exception;

public enum MateExceptionType implements BaseExceptionType{

    MATE_NOT_FOUND(404, "등록된 메이트 모집글 정보가 없습니다.");

    private int errorCode;
    private String errorMsg;

    MateExceptionType(int errorCode, String errorMsg){
        this.errorCode=errorCode;
        this.errorMsg=errorMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }
}
