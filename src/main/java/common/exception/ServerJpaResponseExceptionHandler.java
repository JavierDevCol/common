package common.exception;

import common.response.Responses;
import common.types.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

public abstract class ServerJpaResponseExceptionHandler extends ServerResponseExceptionHandler {
    public ServerJpaResponseExceptionHandler() {
    }

    @ExceptionHandler({
            EmptyResultDataAccessException.class,
            DataAccessResourceFailureException.class,
            ConstraintViolationException.class,
            DataIntegrityViolationException.class})
    public final ResponseEntity<Object> handleDBFailures(Exception ex, WebRequest request) throws Exception {
        if (ex instanceof EmptyResultDataAccessException) {
            return this.handleEmptyResultDataFailure((EmptyResultDataAccessException) ex, request);
        }
        else if (ex instanceof DataAccessResourceFailureException) {
            return this.handleDataBaseConnectionFailures((DataAccessResourceFailureException) ex, request);
        }
        else if (ex instanceof DataIntegrityViolationException) {
            return this.handleDataIntegrityViolationFailure((DataIntegrityViolationException) ex, request);
        }
        else {
            return ex instanceof ConstraintViolationException ? this.handleConstraintViolationFailure(
                    (ConstraintViolationException) ex, request) : this.handleException(ex, request);
        }
    }

    protected ResponseEntity<Object> handleEmptyResultDataFailure(EmptyResultDataAccessException ex, WebRequest request) {
        String message = this.getMessageSource().getMessage(
                "polaris.commons.not.found", (Object[]) null, LocaleContextHolder.getLocale());
        ErrorResponse error = Responses.error(HttpStatus.NOT_FOUND, message, ex);
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleDataBaseConnectionFailures(DataAccessResourceFailureException ex, WebRequest request) {
        String message = this.getMessageSource().getMessage(
                "polaris.commons.error.db.connection", (Object[]) null, LocaleContextHolder.getLocale());
        ErrorResponse error = Responses.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex);
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleConstraintViolationFailure(ConstraintViolationException ex, WebRequest request) {
        String message = this.getMessageSource().getMessage(
                "polaris.commons.error.db.constraints", (Object[]) null, LocaleContextHolder.getLocale());
        ErrorResponse error = Responses.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex);
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleDataIntegrityViolationFailure(DataIntegrityViolationException ex, WebRequest request) {
        String message = this.getMessageSource().getMessage(
                "polaris.commons.error.db.constraints", (Object[]) null, LocaleContextHolder.getLocale());
        ErrorResponse error = Responses.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex);
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }
}
