package com.myalley.member.exception;

public interface BaseExceptionType {
    int getResultCode();
    String getErrType();
    String getErrData();

    String getMsg();
}
