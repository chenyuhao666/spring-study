package com.cyh.spring.formework.annotation;


import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPService {

    String value() default "";

}
