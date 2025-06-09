package com.example.ubiquolibrary.webClient;

import java.lang.annotation.*;

//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.TYPE)
//@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithUbiquoWebClient {
    String name();
    //String baseUri();
    String ubiquoServer();
    String serviceName();
    String sutName();
    boolean integrationMode();
}
