package raze.spring.inventory.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import raze.spring.inventory.Exception.EmailNotFoundException;
import raze.spring.inventory.Exception.IllegalTokenException;
import raze.spring.inventory.Exception.UnauthorizedException;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
@Slf4j
public class ExceptionController extends ResponseEntityExceptionHandler {



    @ExceptionHandler(value = {IllegalTokenException.class})
    protected ResponseEntity<Object> handleIllegalToken (Exception e, WebRequest request) {
        return  handleExceptionInternal(
                e,
                e.getMessage(),
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                request
        );
    }
//
//    @ExceptionHandler(value = {UnauthorizedException.class})
//    protected ResponseEntity<Object> handleUnauthorizedException (Exception e, WebRequest request) {
//        return  handleExceptionInternal(
//                e,
//                e.getMessage(),
//                new HttpHeaders(),
//                HttpStatus.UNAUTHORIZED,
//                request
//        );
//    }



    @ExceptionHandler(value = {EmailNotFoundException.class})
    protected ResponseEntity<Object> handleEmailNotFound (Exception e, WebRequest request) {
        return  handleExceptionInternal(
          e,
           e.getMessage(),
           new HttpHeaders(),
           HttpStatus.NOT_FOUND,
           request
        );
    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(Exception ex, WebRequest webRequest) {
        log.debug("Catching NoSuchElementException");

        return handleExceptionInternal(
            ex,
            "Object not found, Please try later",
            new HttpHeaders(),
            HttpStatus.FORBIDDEN,
            webRequest);
    }
    @ExceptionHandler({UsernameNotFoundException.class, IllegalStateException.class})
    protected ResponseEntity<Object> handleIllegalException(Exception ex, WebRequest webRequest) {
        return  handleExceptionInternal(
                ex,
                ex.getMessage(),
                new HttpHeaders(),
                HttpStatus.UNAUTHORIZED,
                webRequest
        );
    }


    @ExceptionHandler({TransactionSystemException.class})
    protected ResponseEntity<Object> handlePersistenceException(final Exception ex, final WebRequest request) {
        logger.info(ex.getClass().getName());
        //
        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException) {

            ConstraintViolationException consEx= (ConstraintViolationException) cause;
            final List<String> errors = new ArrayList<String>();
            for (final ConstraintViolation<?> violation : consEx.getConstraintViolations()) {
                errors.add(violation.getPropertyPath() + ": " + violation.getMessage());
            }

            return new ResponseEntity<Object>(errors, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("error occurred", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ArrayList<String> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(new Date() + fieldError.getField() + fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
