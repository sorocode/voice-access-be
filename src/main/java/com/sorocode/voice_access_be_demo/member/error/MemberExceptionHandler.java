package com.sorocode.voice_access_be_demo.member.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // 요청 JSON 형식 오류
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MemberErrorResponse> handleInvalidRequest(HttpMessageNotReadableException e) {
        MemberErrorResponse error = new MemberErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("잘못된 요청 형식입니다. 날짜나 숫자 등의 형식을 확인해주세요.");
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // @Valid 등 검증 실패 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MemberErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String defaultMessage = e.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .orElse("잘못된 입력 값이 있습니다.");

        MemberErrorResponse error = new MemberErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(defaultMessage);
        error.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
