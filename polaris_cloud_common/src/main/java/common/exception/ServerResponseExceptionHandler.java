package common.exception;


import common.response.Responses;
import common.types.ErrorResponse;
import common.types.ValidationErrorResponse;
import common.util.UtilJson;
import common.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.NotSupportedException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import org.apache.log4j.Logger;

public abstract class ServerResponseExceptionHandler extends ResponseEntityExceptionHandler {
    //private final Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private MessageSource messageSource;

    public ServerResponseExceptionHandler() {
    }

    @ExceptionHandler({
            NotAuthorizedException.class,
            NotFoundException.class,
            NotFoundObjectException.class,
            HttpClientErrorException.class,
            HttpServerErrorException.class,
            ConversionFailedException.class,
            ConverterNotFoundException.class,
            InvalidEnumValueException.class,
            ValidationException.class,
            InvocationTargetException.class,
            IllegalArgumentException.class,
            BadRequestException.class,
            NotSupportedException.class,
            IllegalStateException.class,
            ErrorResponseException.class})
    public final ResponseEntity<Object> handleCommonWebFailures(Exception ex, WebRequest request) {
        if (ex instanceof NotFoundObjectException) {
            return this.handleNotFoundObjectFailure((NotFoundObjectException) ex, request);
        }
        else if (ex instanceof NotFoundException) {
            return this.handleNotFoundFailure((NotFoundException) ex, request);
        }
        else if (ex instanceof NotAuthorizedException) {
            return this.handleNotAuthorizedFailure((NotAuthorizedException) ex, request);
        }
        else if (ex instanceof HttpClientErrorException) {
            return this.handleClientErrorFailure((HttpClientErrorException) ex, request);
        }
        else if (ex instanceof HttpServerErrorException) {
            return this.handleServerErrorFailure((HttpServerErrorException) ex, request);
        }
        else if (ex instanceof ConversionFailedException) {
            return this.handleConversionFailure((ConversionFailedException) ex, request);
        }
        else if (ex instanceof ConverterNotFoundException) {
            return this.handleConverterNotFoundFailure((ConverterNotFoundException) ex, request);
        }
        else if (ex instanceof InvalidEnumValueException) {
            return this.handleInvalidEnumValueFailure((InvalidEnumValueException) ex, request);
        }
        else if (ex instanceof ValidationException) {
            return this.handleValidationFailure((ValidationException) ex, request);
        }
        else {
            return ex instanceof ErrorResponseException ? this.handleErrorResponseFailure(
                    (ErrorResponseException) ex, request) : this.handleMiscFailures(ex, request);
        }
    }

    protected ResponseEntity<Object> handleServerErrorFailure(HttpServerErrorException ex, WebRequest request) {
        String bodyString = ex.getResponseBodyAsString();
        ErrorResponse error = (ErrorResponse) UtilJson.toObject(bodyString, ErrorResponse.class);
        if (error == null) {
            error = Responses.error(ex.getStatusCode(), ex.getResponseBodyAsString());
        }

        return this.handleExceptionInternal(ex, error, ex.getResponseHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleClientErrorFailure(HttpClientErrorException ex, WebRequest request) {
        String bodyString = ex.getResponseBodyAsString();
        ErrorResponse error = (ErrorResponse) UtilJson.toObject(bodyString, ErrorResponse.class);
        if (error == null) {
            error = Responses.error(ex.getStatusCode(), ex.getResponseBodyAsString());
        }

        return this.handleExceptionInternal(ex, error, ex.getResponseHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleNotFoundFailure(NotFoundException ex, WebRequest request) {
        ErrorResponse error = Responses.error(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleNotFoundObjectFailure(NotFoundObjectException ex, WebRequest request) {
        String message = this.messageSource.getMessage(
                "polaris.commons.not.found.object", new Object[]{ex.getObjectId()}, LocaleContextHolder.getLocale());
        ErrorResponse error = Responses.error(HttpStatus.NOT_FOUND, message);
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleNotAuthorizedFailure(NotAuthorizedException ex, WebRequest request) {
        ErrorResponse error = Responses.error(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleConversionFailure(ConversionFailedException ex, WebRequest request) {
        if (ex.getRootCause() instanceof NotFoundException) {
            return this.handleNotFoundFailure((NotFoundException) ex.getCause(), request);
        }
        else {
            String message = this.messageSource.getMessage(
                    "polaris.commons.error.unexpected", (Object[]) null, LocaleContextHolder.getLocale());
            ErrorResponse error = Responses.error(HttpStatus.BAD_REQUEST, message, ex);
            return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
        }
    }

    protected ResponseEntity<Object> handleConverterNotFoundFailure(ConverterNotFoundException ex, WebRequest request) {
        String message = this.messageSource.getMessage(
                "polaris.commons.error.converter.not-found", (Object[]) null, LocaleContextHolder.getLocale());
        ErrorResponse error = Responses.error(HttpStatus.INTERNAL_SERVER_ERROR, message, ex);
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleInvalidEnumValueFailure(InvalidEnumValueException ex, WebRequest request) {
        String message = this.messageSource.getMessage(
                "polaris.commons.validation.enum.invalid", new Object[]{ex.getValue()}, LocaleContextHolder.getLocale());
        String msg = this.messageSource.getMessage(
                "polaris.commons.validation.message.default", (Object[]) null, LocaleContextHolder.getLocale());
        ValidationError error = new ValidationError(ex.getField(), message);
        ValidationErrorResponse response = Responses.validation(error, msg);
        return this.handleExceptionInternal(ex, response, new HttpHeaders(), response.getStatus(), request);
    }

    protected ResponseEntity<Object> handleValidationFailure(ValidationException ex, WebRequest request) {
        String message = this.messageSource.getMessage(
                "polaris.commons.validation.message.default", (Object[]) null, LocaleContextHolder.getLocale());
        ValidationErrorResponse response = Responses.validation(ex.getErrors(), message);
        return this.handleExceptionInternal(ex, response, new HttpHeaders(), response.getStatus(), request);
    }

    protected ResponseEntity<Object> handleMiscFailures(Exception ex, WebRequest request) {
        String message = this.messageSource.getMessage(
                "polaris.commons.error.unexpected", (Object[]) null, LocaleContextHolder.getLocale());
        ErrorResponse error = Responses.error(HttpStatus.BAD_REQUEST, message, ex);
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleErrorResponseFailure(ErrorResponseException ex, WebRequest request) {
        ErrorResponse error = ex.getErrorResponse();
        return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ValidationError> errors = new ArrayList();
        BindingResult result = ex.getBindingResult();

        ValidationError error;
        for (Iterator var7 = result.getAllErrors().iterator(); var7.hasNext(); errors.add(error)) {
            ObjectError objectError = (ObjectError) var7.next();
            error = new ValidationError();
            error.setMessage(objectError.getDefaultMessage());
            if (objectError instanceof FieldError) {
                error.setField(((FieldError) objectError).getField());
            }
        }

        String message = this.messageSource.getMessage(
                "polaris.commons.validation.message.default", (Object[]) null, LocaleContextHolder.getLocale());
        return this.handleExceptionInternal(ex, Responses.validation(errors, message), headers, status, request);
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getRootCause() instanceof InvalidEnumValueException) {
            return this.handleInvalidEnumValueFailure((InvalidEnumValueException) ex.getRootCause(), request);
        }
        else {
            String message = this.messageSource.getMessage(
                    "polaris.commons.validation.json.invalid", (Object[]) null, LocaleContextHolder.getLocale());
            ErrorResponse error = Responses.error(HttpStatus.BAD_REQUEST, message);
            return this.handleExceptionInternal(ex, error, new HttpHeaders(), error.getStatus(), request);
        }
    }

    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        this.logger.debug(ex.getLocalizedMessage(), ex);
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    protected MessageSource getMessageSource() {
        return this.messageSource;
    }
}

