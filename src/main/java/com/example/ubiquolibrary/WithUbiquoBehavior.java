package com.example.ubiquolibrary;

import com.example.ubiquolibrary.model.ClientType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithUbiquoBehavior {
    String client();
    String clientName();
//    ClientType type();
    String ubiquo();
    String sutName();
}
