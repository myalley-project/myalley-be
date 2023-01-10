package com.myalley.exception;

import lombok.Getter;

public enum ExhibitionExceptionType {
    FILE_NOT_FOUND(404, "이미지를 찾을 수 없습니다"),
    FILE_TYPE_NOT_ACCEPTED(406, "허용되지 않은 타입의 파일입니다."),
    FILE_NOT_UPLOADED(400, "이미지가 첨부되지 않았습니다."),
    EXHIBITION_NOT_FOUND(400, "등록된 전시회 정보가 없습니다.");

    @Getter
    private int code;

    @Getter
    private String message;

    ExhibitionExceptionType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
