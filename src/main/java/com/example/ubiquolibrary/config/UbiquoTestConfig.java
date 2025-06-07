package com.example.ubiquolibrary.config;

import com.example.ubiquolibrary.WithUbiquoBehaviorPostProcessor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UbiquoTestConfig {

    @Bean
    public WithUbiquoBehaviorPostProcessor withBehaviorPostProcessor() {
        return new WithUbiquoBehaviorPostProcessor();
    }
}