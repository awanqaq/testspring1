package com.yc.springframework.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({TYPE, FIELD, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyResource {
    public String name() default "";
}
