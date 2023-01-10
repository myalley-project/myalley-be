package com.myalley.exception;

public interface BaseExceptionType {
    int getResultCode();
    String getErrType();
    String getErrData();

    String getMsg();
}
