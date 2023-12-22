package com.backend.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalNotNullValidator.class)
public @interface ConditionalNotNull {

    String message() default "Attribute should not be null.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}