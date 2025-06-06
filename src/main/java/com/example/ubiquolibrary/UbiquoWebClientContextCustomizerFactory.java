package com.example.ubiquolibrary;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

public class UbiquoWebClientContextCustomizerFactory implements ContextCustomizerFactory {
    @Override
    public ContextCustomizer createContextCustomizer(Class<?> testClass, List<ContextConfigurationAttributes> configAttributes) {
        WithUbiquoWebClients annotation = AnnotatedElementUtils.findMergedAnnotation(testClass, WithUbiquoWebClients.class);
        if (annotation == null) return null;

        Map<String, String> overrides = Arrays.stream(annotation.value())
                .collect(Collectors.toMap(WithUbiquoWebClient::name, WithUbiquoWebClient::baseUri));

        return (context, mergedConfig) -> context.addBeanFactoryPostProcessor(beanFactory -> {
            if (!(beanFactory instanceof BeanDefinitionRegistry registry)) {
                throw new IllegalStateException("BeanFactory is not a BeanDefinitionRegistry");
            }

            for (Map.Entry<String, String> entry : overrides.entrySet()) {
                String beanName = entry.getKey();
                String baseUri = entry.getValue();

                if (registry.containsBeanDefinition(beanName)) {
                    registry.removeBeanDefinition(beanName);
                    System.out.println("Removed existing WebClient bean: " + beanName);
                }

                RootBeanDefinition def = new RootBeanDefinition(WebClient.class, () ->
                        WebClient.builder().baseUrl(baseUri).build());
                def.setPrimary(true); // Optional: mark as primary
                registry.registerBeanDefinition(beanName, def);

                System.out.printf("Registered overridden WebClient '%s' with URI '%s'%n", beanName, baseUri);
            }
        });
    }
}


//    @Override
//    public ContextCustomizer createContextCustomizer(Class<?> testClass,
//            List<ContextConfigurationAttributes> configAttributes) {
//        List<WithUbiquoWebClient> clientDefs = new ArrayList<>();
//
//        WithUbiquoWebClient single = AnnotationUtils.findAnnotation(testClass, WithUbiquoWebClient.class);
//        if (single != null) clientDefs.add(single);
//
//        WithUbiquoWebClients multiple = AnnotationUtils.findAnnotation(testClass, WithUbiquoWebClients.class);
//        if (multiple != null) {
//            for (WithUbiquoWebClient c : multiple.value()) {
//                clientDefs.add(c);
//            }
//        }
//
//        if (clientDefs.isEmpty()) return null;
//
//        return (context, mergedConfig) -> {
//            for (WithUbiquoWebClient def : clientDefs) {
//                String name = def.name();
//                String baseUri = def.baseUri();
//
//                // Log registration
//                System.out.println("✅[TEST] Registering WebClient bean: " + name + " → " + baseUri);
//
//                // Create BeanDefinition
//                GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
//                beanDefinition.setBeanClass(WebClient.class);
//                beanDefinition.setInstanceSupplier(() ->
//                        WebClient.builder().baseUrl(baseUri).build()
//                );
//                beanDefinition.setPrimary(true);
//                // Force override by replacing BeanDefinition
////                ConfigurableListableBeanFactory factory = context.getBeanFactory();
//                ConfigurableListableBeanFactory bf = context.getBeanFactory();
//                System.out.println("Before customizer, contains visitsWebClient? " + bf.containsBeanDefinition("visitsWebClient"));
//
//                BeanDefinitionRegistry reg = (BeanDefinitionRegistry) context.getBeanFactory();
//                if (reg.containsBeanDefinition(name)) {
//                    reg.removeBeanDefinition(name);
//                }
//                reg.registerBeanDefinition(name, beanDefinition);
//                System.out.println("After customizer, contains visitsWebClient? " + bf.containsBeanDefinition("visitsWebClient"));
//
////                if (factory instanceof BeanDefinitionRegistry registry) {
////                    // Log registration
////                    registry.removeBeanDefinition(name); // optional: remove existing if needed
////                    registry.registerBeanDefinition(name, beanDefinition);
////                } else {
////                    factory.registerSingleton(name, WebClient.builder().baseUrl(baseUri).build());
////                }
//            }
//        };
//
////        return (context, mergedConfig) -> {
////            for (WithUbiquoWebClient def : clientDefs) {
////                context.getBeanFactory().registerSingleton(
////                        def.name(),
////                        WebClient.builder().baseUrl(def.baseUri()).build()
////                );
////                System.out.println("[TEST-OVERRIDE] Registering: " + def.name() + " → " + def.baseUri());
////            }
////        };
//    }
