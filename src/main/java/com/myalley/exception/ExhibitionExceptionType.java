package com.myalley.exception;


public enum ExhibitionExceptionType implements BaseExceptionType{
    FILE_NOT_FOUND(404, "이미지를 찾을 수 없습니다."),
    FILE_TYPE_NOT_ACCEPTED(400, "허용되지 않은 타입의 파일 형식입니다."),
    FILE_NOT_UPLOADED(400, "이미지 파일이 첨부되지 않았습니다."),
    EXHIBITION_NOT_FOUND(404, "전시회 정보를 찾을 수 없습니다."),
    MATE_NOT_FOUND(404, "메이트 모집글 정보를 찾을 수 없습니다.");

    private int errorCode;
    private String errorMsg;

    ExhibitionExceptionType(int errorCode, String errorMsg){
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

