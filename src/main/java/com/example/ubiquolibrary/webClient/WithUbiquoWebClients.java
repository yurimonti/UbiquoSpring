package com.example.ubiquolibrary.webClient;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(UbiquoWebClientRegistrar.class)
public @interface WithUbiquoWebClients {
    WithUbiquoWebClient[] value();
}
