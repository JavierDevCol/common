package com.heystyles.common.exception;

public class InvalidEnumValueException extends RuntimeException {
    private String field;
    private String value;

    public InvalidEnumValueException(String field, String value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return this.field;
    }

    public String getValue() {
        return this.value;
    }
}
