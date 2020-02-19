package com.heystyles.common.service;

import com.heystyles.common.validation.ValidationError;

import java.util.List;

public interface ValidationService {
    <T> void validate(T var1, Class<?> var2);

    <T> List<ValidationError> validateAndGet(T var1, Class<?> var2);
}

