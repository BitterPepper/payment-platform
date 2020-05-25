package com.evolut.payment.utils;

import com.evolut.payment.dto.CreateAccountReq;
import com.google.inject.Singleton;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class ValidationHelper {
    private Validator validator;

    public ValidationHelper() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public void checkAccount(Object checkObject, String checkDescription) throws ValidationException {
        Set<ConstraintViolation<Object>> violations = validator.validate(checkObject);
        if (violations.size() != 0) {
            String violationMessage = checkDescription + violations.stream()
                    .map(v -> v.getPropertyPath() + " " + v.getMessage()).collect(Collectors.joining("; "));
            throw new ValidationException(violationMessage);
        }
    }

}
