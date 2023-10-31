package com.cstudy.moduleapi.exception;

import com.cstudy.modulecommon.error.abstracts.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.mail.MessagingException;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

    /**
     *  javax.validation.Valid or @Validated 으로 binding error 발생시 발생한다.
     *  HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
     *  주로 @RequestBody, @RequestPart 어노테이션에서 발생
     */
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

        log.error("handleMethodArgumentNotValidException : {} , StatusCode : {}", e, response.getCode());

        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolation(DataIntegrityViolationException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message(e.getMessage())
                .build();

        log.error("DataIntegrityViolationException : {} , StatusCode : {}", e, response.getCode());
        response.addValidation("유니크 키 중복 데이터 삽입", e.getMessage());

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
        log.error("MessagingException : {} , StatusCode : {}", e, response.getCode());
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse AccessDeniedException(AccessDeniedException e) {
        ErrorResponse response = ErrorResponse.builder()
                .code("403")
                .message("Access Denied: " + e.getMessage())
                .build();

        response.addValidation("Spring Security", "권한이 일치하지 않습니다.");
        log.error("AccessDeniedException : {} , StatusCode : {}", e, response.getCode());
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
        log.error("MemberException : {} , StatusCode : {}", e, body.getCode());
        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PathvariableAbstractException.class)
    public ErrorResponse PathvariableAbstractException(PathvariableAbstractException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .build();

        body.addValidation("PathvariableAbstractException", "id는 양수로 처리를 해야됩니다.");
        log.error("PathvariableAbstractException : {} , StatusCode : {}", e, body.getCode());
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
        log.error("QuestionException : {} , StatusCode : {}", e, body.getCode());
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
        log.error("WorkbookException : {} , StatusCode : {}", e, body.getCode());
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
        log.error("CommentAbstractException : {} , StatusCode : {}", e, body.getCode());
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
        log.error("RequestException : {} , StatusCode : {}", e, body.getCode());
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
        log.error("Competition : {} , StatusCode : {}", e, body.getCode());
        return ResponseEntity.status(statusCode)
                .body(body).getBody();
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlarmAbstractException.class)
    public ErrorResponse AlarmAbstractException(AlarmAbstractException e) {

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(e.getStatusCode()))
                .message(e.getMessage())
                .build();

        body.addValidation("AlarmAbstractException", "Alarm 관련 Exception");
        log.error("AlarmAbstractException : {} , StatusCode : {}", e, body.getCode());
        return ResponseEntity.status(e.getStatusCode())
                .body(body).getBody();
    }


    @ExceptionHandler(HttpMessageConversionException.class)
    protected ErrorResponse handleHttpMessageConversionException(HttpMessageConversionException e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .build();

        body.addValidation("json의 형식이 잘못됨", "body json의 형식을 확인을 해주세요");
        log.error("AlarmAbstractException : {} , StatusCode : {}", e, body.getCode());
        return ResponseEntity.status(HttpStatus.valueOf(e.getMessage()))
                .body(body).getBody();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .build();

        body.addValidation("json의 형식이 잘못됨", "body json의 형식을 확인을 해주세요");
        log.error("AlarmAbstractException : {} , StatusCode : {}", e, body.getCode());
        return ResponseEntity.status(HttpStatus.valueOf(e.getMessage()))
                .body(body).getBody();
    }
    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ErrorResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        ErrorResponse body = ErrorResponse.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .message(e.getMessage())
                .build();

        body.addValidation("지원하지 않은 HTTP method 호출 할 경우 발생", "지원하지 않은 HTTP method 호출 할 경우 발생");
        return ResponseEntity.status(HttpStatus.valueOf(e.getMessage()))
                .body(body).getBody();
    }
}
