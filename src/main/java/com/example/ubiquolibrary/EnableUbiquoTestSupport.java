package com.example.ubiquolibrary;

import com.example.ubiquolibrary.config.UbiquoTestConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(UbiquoTestConfig.class)
public @interface EnableUbiquoTestSupport {
}
