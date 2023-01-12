package com.myalley.exception;


public class CustomException extends BaseException{

    private BaseExceptionType exceptionType;

    public CustomException(BaseExceptionType exceptionType){
        this.exceptionType=exceptionType;
    }

    @Override
    public BaseExceptionType getExceptionType() {
        return exceptionType;
    }
}
