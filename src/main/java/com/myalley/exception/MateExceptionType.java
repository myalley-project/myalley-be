package com.myalley.exception;

public enum MateExceptionType implements BaseExceptionType{

    MATE_NOT_FOUND(404, "등록된 메이트 모집글 정보가 없습니다."),
    UNAUTHORIZED_ACCESS(403, "해당 게시글의 수정 권한이 없습니다."),
    CANNOT_BOOKMARK_MY_POST(400, "본인이 작성한 글은 북마크 목록에 추가할 수 없습니다."),
    COMMENT_NOT_FOUND(400, "댓글 정보를 찾을 수 없습니다."),
    NOT_EXIST_REPLIES(400, "답글이 존재하지 않습니다."),
    CANNOT_WRITE_REPLY(404, "대댓글에 답글을 작성할 수 없습니다."),
    MEMBER_ID_IS_MANDATORY(404, "memberId는 헤더에 반드시 포함되어야 합니다.");

    private int errorCode;
    private String errorMsg;

    MateExceptionType(int errorCode, String errorMsg){
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
