package raze.spring.inventory.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

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


}
