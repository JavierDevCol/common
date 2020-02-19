package com.heystyles.common.service.impl;


import com.heystyles.common.exception.ValidationException;
import com.heystyles.common.service.ValidationService;
import com.heystyles.common.validation.ValidationError;
import com.heystyles.common.validation.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ValidationServiceImpl implements ValidationService, InitializingBean {
    @Autowired(
            required = false
    )
    private Set<Validator<?>> validators;
    private Map<Class<?>, Map<Class<?>, List<Validator<?>>>> validatorsByScopeAndType = new HashMap();

    public ValidationServiceImpl() {
    }

    public <T> void validate(T object, Class<?> scope) {
        List<ValidationError> errors = this.validateAndGet(object, scope);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public <T> List<ValidationError> validateAndGet(T object, Class<?> scope) {
        List<ValidationError> errors = new ArrayList();
        List<Validator<T>> validatorsList = this.getValidatorsOfObjectByScope(object, scope);
        Iterator var5 = validatorsList.iterator();

        while (var5.hasNext()) {
            Validator<T> validator = (Validator) var5.next();
            List<ValidationError> temp = validator.validate(object);
            errors.addAll(temp);
            if (validator.isBreakingValidation()) {
                break;
            }
        }

        return errors;
    }

    public void afterPropertiesSet() {
        if (this.validators != null) {
            Iterator var1 = this.validators.iterator();

            while (var1.hasNext()) {
                Validator<?> validator = (Validator) var1.next();
                this.addValidator(validator);
            }
        }

    }

    private <T> List<Validator<T>> getValidatorsOfObjectByScope(T object, Class<?> scope) {
        return new ArrayList((Collection) ((Map) this.validatorsByScopeAndType.computeIfAbsent(object.getClass(), (c) -> {
            return new HashMap();
        })).computeIfAbsent(scope, (s) -> {
            return new ArrayList();
        }));
    }

    private void addValidator(Validator<?> validator) {
        Class<?> type = (Class) ((ParameterizedType) validator.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        Map<Class<?>, List<Validator<?>>> validatorsByScope = (Map) this.validatorsByScopeAndType.computeIfAbsent(type, (t) -> {
            return new HashMap();
        });
        Iterator var4 = validator.getScopes().iterator();

        while (var4.hasNext()) {
            Class<?> scope = (Class) var4.next();
            List<Validator<?>> validatorsList = (List) validatorsByScope.computeIfAbsent(scope, (s) -> {
                return new ArrayList();
            });
            validatorsList.add(validator);
        }

    }

    public void setValidators(Set<Validator<?>> validators) {
        this.validators = validators;
    }
}
