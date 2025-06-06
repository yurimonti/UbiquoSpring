package com.example.ubiquolibrary.annotation;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface WithUbiquoRestClients {
    WithUbiquoRestClient[] value();
}
