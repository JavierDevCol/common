package com.heystyles.common.exception;

import com.heystyles.common.types.ErrorResponse;

public class ErrorResponseException extends RuntimeException {
    private final ErrorResponse errorResponse;

    public ErrorResponseException(ErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public ErrorResponse getErrorResponse() {
        return this.errorResponse;
    }
}

