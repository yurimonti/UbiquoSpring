package com.example.ubiquolibrary.annotation;

import com.example.ubiquolibrary.registrar.UbiquoRestClientRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(UbiquoRestClientRegistrar.class)
public @interface WithUbiquoRestClient {
    String name() default "defaultRestClient";
    String baseUri();
}
