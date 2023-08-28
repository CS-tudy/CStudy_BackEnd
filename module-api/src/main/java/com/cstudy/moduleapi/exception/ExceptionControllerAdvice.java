package com.cstudy.moduleapi.exception;

import com.cstudy.modulecommon.error.abstracts.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.mail.MessagingException;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MessagingException.class)
    public ErrorResponse invalidRequestHandler(MessagingException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message(e.getMessage())
                .build();

        response.addValidation("회원가입 Email", e.getMessage());

        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse AccessDeniedException(AccessDeniedException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("403")
                .message("Access Denied: "+e.getMessage())
                .build();
        response.addValidation("Spring Security", "권한이 일치하지 않습니다.");
        return response;
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberAbstractException.class)
    public ErrorResponse MemberException(MemberAbstractException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        body.addValidation("MemberException", "회원 관련 Exception");

        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(QuestionAbstractException.class)
    public ErrorResponse QuestionAbstractException(QuestionAbstractException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        body.addValidation("QuestionException", "문제 관련 Exception");

        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WorkbookAbstractException.class)
    public ErrorResponse WorkbookAbstractException(WorkbookAbstractException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        body.addValidation("WorkbookException", "문제집 관련 Exception");

        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }

    //CommentAbstractException
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CommentAbstractException.class)
    public ErrorResponse CommentAbstractException(CommentAbstractException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        body.addValidation("CommentAbstractException", "댓글 관련 Exception");

        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RequestAbstractException.class)
    public ErrorResponse RequestAbstractException(RequestAbstractException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        body.addValidation("RequestException", "요청 관련 Exception");

        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CompetitionAbstractException.class)
    public ErrorResponse CompetitionAbstractException(CompetitionAbstractException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        body.addValidation("Competition", "대회 관련 Exception");

        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }

}
