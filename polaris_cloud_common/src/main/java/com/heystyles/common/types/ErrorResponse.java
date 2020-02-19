package com.heystyles.common.types;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heystyles.common.util.UtilStackTrace;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ErrorResponse extends BaseResponse {
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createdDate;
    private String debugMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(@NotNull HttpStatus status, String message) {
        this(status.value(), message);
    }

    public ErrorResponse(int statusCode, String message) {
        this(statusCode, message, (Throwable) null);
    }

    public ErrorResponse(@NotNull HttpStatus status, Throwable ex) {
        this(status.value(), ex);
    }

    public ErrorResponse(int statusCode, Throwable ex) {
        this(statusCode, ex.getLocalizedMessage(), ex);
    }

    public ErrorResponse(HttpStatus status, String message, Throwable ex) {
        this(status.value(), message, ex);
    }

    public ErrorResponse(int statusCode, String message, Throwable ex) {
        super(false, statusCode, message);
        this.createdDate = LocalDateTime.now();
        this.debugMessage = UtilStackTrace.getStackTrace(ex);
    }

    public LocalDateTime getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getDebugMessage() {
        return this.debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }
}

