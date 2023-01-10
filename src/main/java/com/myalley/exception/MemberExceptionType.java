package com.myalley.exception;

public enum MemberExceptionType implements BaseExceptionType{

    ALREADY_EXIST_USERNAME(400,"exist","email","이메일이 존재합니다."),
    WRONG_PASSWORD(400,"exist","password","잘못된 비밀번호 입니다."),
    NOT_FOUND_MEMBER(400,"exist","email","존재하지않는 계정입니다."),
    ALREADY_EXIST_NICKNAME(400,"exist","nickname","닉네임이 중복되었습니다."),
    ALREADY_EXIST_NAME(400,"exist","name","이름이 중복되었습니다."),
    ALREADY_EXIST_ADMINNO(400,"exist","adminNo","잘못된 고유번호입니다.");


    private int resultCode;
    private String errType;
    private String errData;

    private String msg;

    MemberExceptionType(int resultCode,String errType,String errData,String msg){
        this.resultCode=resultCode;
        this.errType=errType;
        this.errData=errData;
        this.msg=msg;
    }



    @Override
    public int getResultCode() {
        return this.resultCode;
    }

    @Override
    public String getErrType() {
        return this.errType;
    }

    @Override
    public String getErrData() {
        return this.errData;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
