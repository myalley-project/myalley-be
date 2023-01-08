package com.myalley.member.exception;

import lombok.RequiredArgsConstructor;


public class MemberException extends BaseException{

    private BaseExceptionType exceptionType;

    public MemberException(BaseExceptionType exceptionType){
        this.exceptionType=exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
