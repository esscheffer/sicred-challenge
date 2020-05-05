package com.erikscheffer.sicredchallenge.exceptionhandler;

import com.erikscheffer.sicredchallenge.exception.ClosedVoteSessionException;
import com.erikscheffer.sicredchallenge.exception.DuplicateVoteException;
import com.erikscheffer.sicredchallenge.exception.EmptyVoteSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public ApplicationExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String userMessage = messageSource.getMessage("message.invalid", null, LocaleContextHolder.getLocale());

        String developerMessage;
        if (ex.getCause() != null) {
            developerMessage = ex.getCause().toString();
        } else {
            developerMessage = ex.toString();
        }

        return handleExceptionInternal(ex,
                Collections.singletonList(new ErrorMessageWrapper(userMessage, developerMessage)),
                headers,
                HttpStatus.BAD_REQUEST,
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return handleExceptionInternal(ex,
                createErrorMessageWrappers(ex.getBindingResult()),
                headers,
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String userMessage = messageSource.getMessage("resource.not-found", null, LocaleContextHolder.getLocale());
        return handleExceptionInternal(
                ex,
                Collections.singletonList(new ErrorMessageWrapper(userMessage, ex.toString())),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }

    @ExceptionHandler(DuplicateVoteException.class)
    ResponseEntity<Object> handleDuplicateVoteException(DuplicateVoteException ex, WebRequest request) {
        String userMessage = messageSource.getMessage(
                "message.duplicate-vote.errorMessage",
                new Object[]{ex.getIdAssociate(), ex.getVoteSession().getId()},
                LocaleContextHolder.getLocale());
        return handleExceptionInternal(
                ex,
                Collections.singletonList(new ErrorMessageWrapper(userMessage, ex.toString())),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(ClosedVoteSessionException.class)
    ResponseEntity<Object> handleClosedVoteSessionException(ClosedVoteSessionException ex, WebRequest request) {
        String userMessage = messageSource.getMessage(
                "message.vote-session-closed.errorMessage",
                new Object[]{ex.getVoteSession().getId()},
                LocaleContextHolder.getLocale());
        return handleExceptionInternal(
                ex,
                Collections.singletonList(new ErrorMessageWrapper(userMessage, ex.toString())),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    @ExceptionHandler(EmptyVoteSession.class)
    ResponseEntity<Object> handleEmptyVoteSession(EmptyVoteSession ex, WebRequest request) {
        String userMessage = messageSource.getMessage(
                "message.vote-session-closed.errorMessage",
                new Object[]{ex.getVoteSession().getId()},
                LocaleContextHolder.getLocale());
        return handleExceptionInternal(
                ex,
                Collections.singletonList(new ErrorMessageWrapper(userMessage, ex.toString())),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                request);
    }

    private List<ErrorMessageWrapper> createErrorMessageWrappers(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(fieldError -> new ErrorMessageWrapper(messageSource.getMessage(fieldError,
                        LocaleContextHolder.getLocale()),
                        fieldError.toString()))
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorMessageWrapper {
        private final String userMessage;
        private final String developerMessage;
    }
}
