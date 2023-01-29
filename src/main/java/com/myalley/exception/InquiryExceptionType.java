package com.myalley.exception;

public enum InquiryExceptionType implements BaseExceptionType{

    NOT_FOUND_INQUIRY(404,"문의 정보 없음");




    private int errorCode;
    private String errorMsg;




    InquiryExceptionType(int errorCode,String errorMsg){
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


