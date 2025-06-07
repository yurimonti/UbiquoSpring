package com.example.ubiquolibrary.restClient;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithUbiquoRestClient {
    String name();
    String baseUri();
    boolean integrationMode();
}
