package com.myalley.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionAdvice {//예외처리 responseEntity로 return

    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleBaseEx(BaseException exception){//

        //log.error("BaseException errorCode(): {}",exception.getExceptionType().getErrorCode());

        log.error("BaseException errorMsg(): {}",exception.getExceptionType().getErrorMsg());

        return new ResponseEntity(new ExceptionDto(exception.getExceptionType().getErrorCode(),exception.getExceptionType().getErrorMsg()), HttpStatus.valueOf(400));

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleMemberEx(Exception exception){
        exception.printStackTrace();
        return new ResponseEntity(HttpStatus.OK);
    }


    @Data
    @AllArgsConstructor
    static class ExceptionDto {
        private int errorCode;
        private String errMsg;

    }
}
