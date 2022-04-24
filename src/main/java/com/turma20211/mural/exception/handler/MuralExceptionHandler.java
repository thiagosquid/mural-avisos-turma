package com.turma20211.mural.exception.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.turma20211.mural.exception.TagExistsException;
import com.turma20211.mural.utils.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class MuralExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = messageSource.getMessage("invalid.message", null, LocaleContextHolder.getLocale());
        String code = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        List<ErrorResponse> errorResponses = Arrays.asList(new ErrorResponse(message, code));
        return handleExceptionInternal(ex, errorResponses, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ErrorResponse> errorResponses = createErrorList(ex.getBindingResult());
        return handleExceptionInternal(ex, errorResponses, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({JWTVerificationException.class, TokenExpiredException.class, TagExistsException.class})
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex,
                                                                           WebRequest request) {
        String message = ex.getMessage();
        String code = ex.getClass().getSimpleName();
        List<ErrorResponse> errorResponses = Arrays.asList(new ErrorResponse(message, code));
        return handleExceptionInternal(ex, errorResponses, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    private List<ErrorResponse> createErrorList(BindingResult bindingResult) {
        List<ErrorResponse> errorResponses = new ArrayList<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
            String code = fieldError.getCode();
            errorResponses.add(new ErrorResponse(message, code));
        }
        return errorResponses;
    }

}
