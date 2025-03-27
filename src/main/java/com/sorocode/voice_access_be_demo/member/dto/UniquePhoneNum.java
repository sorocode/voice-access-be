package com.sorocode.voice_access_be_demo.member.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePhoneNumValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhoneNum {
    String message() default "이미 존재하는 전화번호입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
