package com.backend.utils;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConditionalNotNullValidator implements ConstraintValidator<ConditionalNotNull, Object> {

    private boolean isRequired;

    @Override
    public void initialize(ConditionalNotNull conditionalNotNull) {
        isRequired = true;
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (isRequired) {
            return o != null;
        }
        return true;
    }

}
