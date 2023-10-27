package com.backend.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.http.MediaType;

import java.lang.annotation.*;
import java.util.List;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CheckForMulitpartFile.class)

public @interface FileNotEmpty {
    public String value() default MediaType.ALL_VALUE;
    public String message() default "{javax.validation.constraints.ValidMediaType.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
