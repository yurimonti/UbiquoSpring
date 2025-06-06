package com.example.ubiquolibrary.registrar;

import com.example.ubiquolibrary.annotation.WithUbiquoRestClient;
import com.example.ubiquolibrary.annotation.WithUbiquoRestClients;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.client.RestClient;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UbiquoRestClientRegistrar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // Collect all @WithUbiquoRestClient instances
        List<WithUbiquoRestClient> clients = new ArrayList<>();

        // Single annotation
        if (importingClassMetadata.hasAnnotation(WithUbiquoRestClient.class.getName())) {
            Map<String, Object> attrs = importingClassMetadata
                    .getAnnotationAttributes(WithUbiquoRestClient.class.getName());
            clients.add(buildFromAttributes(attrs));
        }

        // Multiple annotation
        if (importingClassMetadata.hasAnnotation(WithUbiquoRestClients.class.getName())) {
            Map<String, Object> multiAttrs = importingClassMetadata
                    .getAnnotationAttributes(WithUbiquoRestClients.class.getName());

            Object[] values = (Object[]) multiAttrs.get("value");
            for (Object attr : values) {
                @SuppressWarnings("unchecked")
                Map<String, Object> clientAttrs = (Map<String, Object>) attr;
                clients.add(buildFromAttributes(clientAttrs));
            }
        }

        // Register all clients
        for (WithUbiquoRestClient client : clients) {
            String name = client.name();
            String baseUri = client.baseUri();

            RootBeanDefinition def = new RootBeanDefinition(RestClient.class, () ->
                    RestClient.builder().baseUrl(baseUri).build());

            registry.registerBeanDefinition(name, def);
        }
    }

    // Helper to manually create annotation instances (since we're using raw attributes)
    private WithUbiquoRestClient buildFromAttributes(Map<String, Object> attrs) {
        String name = (String) attrs.get("name");
        String baseUri = (String) attrs.get("baseUri");

        return new WithUbiquoRestClient() {
            @Override
            public String name() { return name; }

            @Override
            public String baseUri() { return baseUri; }

            @Override
            public Class<? extends Annotation> annotationType() {
                return WithUbiquoRestClient.class;
            }
        };
    }
}
