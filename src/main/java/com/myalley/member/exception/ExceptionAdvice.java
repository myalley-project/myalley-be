package com.myalley.member.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseEx(BaseException exception){
//        log.error("BaseException errorType(): {}",exception.getExceptionType().getErrType());
       log.error("BaseException errorCode(): {}",exception.getExceptionType().getResultCode());
//        log.error("BaseException errorData(): {}",exception.getExceptionType().getErrData());
        log.error("BaseException errorMsg(): {}",exception.getExceptionType().getMsg());

        return new ResponseEntity(new ExceptionDto(exception.getExceptionType().getResultCode(),exception.getExceptionType().getErrType(),exception.getExceptionType().getErrData()), HttpStatus.valueOf(400));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleMemberEx(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity(HttpStatus.OK);
    }


    @Data
    @AllArgsConstructor
    static class ExceptionDto {
        private int resultCode;
        private String errType;
        private String errData;
    }
}
