package com.example.ubiquolibrary.restClient;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(UbiquoRestClientRegistrar.class)
public @interface WithUbiquoRestClients {
    WithUbiquoRestClient[] value();
}
