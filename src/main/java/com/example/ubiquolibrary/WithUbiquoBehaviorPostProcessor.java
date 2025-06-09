package com.example.ubiquolibrary;

import com.example.ubiquolibrary.model.ClientType;
import com.example.ubiquolibrary.restClient.WithUbiquoRestClient;
import com.example.ubiquolibrary.restClient.WithUbiquoRestClients;
import com.example.ubiquolibrary.webClient.WithUbiquoWebClient;
import com.example.ubiquolibrary.webClient.WithUbiquoWebClients;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

public class WithUbiquoBehaviorPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        Class<?> beanClass = bean.getClass();
//        WithUbiquoWebClients webAnnotation = beanClass.getAnnotation(WithUbiquoWebClients.class);
//        WithUbiquoRestClients restAnnotation = beanClass.getAnnotation(WithUbiquoRestClients.class);
//        WithUbiquoWebClient[] webClassValue = webAnnotation != null ? webAnnotation.value() : new WithUbiquoWebClient[0];
//        WithUbiquoRestClient[] restClassValue = restAnnotation != null ? restAnnotation.value() : new WithUbiquoRestClient[0];
//        if(webClassValue.length == 0 && restClassValue.length ==0)
//            throw new IllegalArgumentException("@WithUbiquoBehavior cannot be initialized due to missed Rest or Web Clients");
        for (Field field : bean.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(WithUbiquoBehavior.class)) {
                if (!field.getType().equals(UbiquoBehavior.class)) {
                    throw new IllegalArgumentException("@WithUbiquoBehavior is only supported on UbiquoBehavior fields");
                }
                WithUbiquoBehavior annotation = field.getAnnotation(WithUbiquoBehavior.class);
//                String clientName = annotation.clientName();
//                MockBehaviorProperties behaviorProperties = annotation.type() == ClientType.REST_CLIENT ?
//                    GetRestMockBehaviorFromBean(restClassValue, clientName) :
//                    GetWebMockBehaviorFromBean(webClassValue, clientName);
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
//    private record MockBehaviorProperties(String ubiquo, String sutName){};
//
//    private MockBehaviorProperties GetWebMockBehaviorFromBean(WithUbiquoWebClient[] webBeanClients, String clientName) {
//        WithUbiquoWebClient webClient = Arrays.stream(webBeanClients)
//                .filter(w -> Objects.equals(w.serviceName(), clientName))
//                .findFirst().orElseThrow();
//        return new MockBehaviorProperties(webClient.ubiquoServer(), webClient.sutName());
//    }
//
//    private MockBehaviorProperties GetRestMockBehaviorFromBean(WithUbiquoRestClient[] restBeanClients, String clientName) {
//        WithUbiquoRestClient webClient = Arrays.stream(restBeanClients)
//                .filter(w -> Objects.equals(w.serviceName(), clientName))
//                .findFirst().orElseThrow();
//        return new MockBehaviorProperties(webClient.ubiquoServer(), webClient.sutName());
//    }
}
