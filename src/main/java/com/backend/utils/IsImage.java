package com.backend.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CheckForImage.class)


public @interface IsImage {

    public String message() default "An image file is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
