package com.example.ubiquolibrary;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class WithUbiquoBehaviorPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(WithUbiquoBehavior.class)) {
                if (!field.getType().equals(UbiquoBehavior.class)) {
                    throw new IllegalArgumentException("@WithUbiquoBehavior is only supported on UbiquoBehavior fields");
                }

                WithUbiquoBehavior annotation = field.getAnnotation(WithUbiquoBehavior.class);
                UbiquoBehavior behavior = new UbiquoBehavior(
                        annotation.client(),
                        annotation.clientName(),
                        annotation.ubiquo(),
                        annotation.sutName()
                );

                field.setAccessible(true);
                try {
                    field.set(bean, behavior);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject UbiquoBehavior", e);
                }
            }
        }
        return bean;
    }
}
