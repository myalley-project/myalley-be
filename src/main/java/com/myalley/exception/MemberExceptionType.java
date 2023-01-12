package com.myalley.exception;

public enum MemberExceptionType implements BaseExceptionType{

    ALREADY_EXIST_USERNAME(409,"이메일이 존재합니다."),
    ALREADY_EXIST_NICKNAME(409,"닉네임이 중복되었습니다."),
    ALREADY_EXIST_NAME(409,"이름이 중복되었습니다."),
    ALREADY_EXIST_ADMINNO(409,"잘못된 고유번호입니다."),
    WRONG_PASSWORD(404,"잘못된 비밀번호 입니다."),
    NOT_FOUND_MEMBER(404,"존재하지않는 계정입니다.");


    private int errorCode;
    private String errorMsg;




    MemberExceptionType(int errorCode,String errorMsg){
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
