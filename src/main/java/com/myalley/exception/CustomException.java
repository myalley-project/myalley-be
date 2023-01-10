package com.myalley.exception;

import lombok.Getter;

public class CustomException extends RuntimeException {

    @Getter
    private ExhibitionExceptionType exhibitionExceptionType;

    public CustomException(ExhibitionExceptionType exhibitionExceptionType) {
        super(exhibitionExceptionType.getMessage());
        this.exhibitionExceptionType = exhibitionExceptionType;
    }
}
