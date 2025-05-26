package com.sorocode.voice_access_be_demo.member.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MemberExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<MemberErrorResponse> handleMemberNotFound(MemberNotFoundException e) {
        MemberErrorResponse error = new MemberErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value()); // 404
        error.setMessage(e.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // fallback: 기타 예외는 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MemberErrorResponse> handleGenericException(Exception e) {
        MemberErrorResponse error = new MemberErrorResponse();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setMessage("서버 내부 오류가 발생했습니다.");
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
