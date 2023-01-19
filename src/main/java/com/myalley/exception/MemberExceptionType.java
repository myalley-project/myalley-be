package com.myalley.exception;

public enum MemberExceptionType implements BaseExceptionType{

    ALREADY_EXIST_USERNAME(409,"이메일 중복"),
    ALREADY_EXIST_NICKNAME(409,"닉네임 중복"),
    ALREADY_EXIST_NAME(409,"이름 중복"),
    ALREADY_EXIST_ADMINNO(404,"관리자번호 확인 불가"),
    NOT_FOUND_MEMBER(404,"회원 정보 없음"),
    ACESSTOKEN_EXPIRED(403,"ACESS토큰 만료"),
    TOKEN_FORBIDDEN(403,"Forbidden");



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
