package com.example.ubiquolibrary.webClient;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.MessageFormat;
import java.util.Map;

public class UbiquoWebClientRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(
            AnnotationMetadata metadata,
            BeanDefinitionRegistry registry) {

        Map<String, Object> attributes = metadata.getAnnotationAttributes(WithUbiquoWebClients.class.getName());
        if (attributes == null) return;

        AnnotationAttributes[] clientsAttributes = (AnnotationAttributes[]) attributes.get("value");
        for (AnnotationAttributes clientAttr  : clientsAttributes) {
            String beanName = clientAttr.getString("name");
            String ubiquoHost = clientAttr.getString("ubiquoServer");
            String serviceName = clientAttr.getString("serviceName");
            String sutName = clientAttr.getString("sutName");
            boolean isIntegrationMode = clientAttr.getBoolean("integrationMode");
            String secondPartUri = "/api/v2";
            String pathVariableUri = MessageFormat.format("/{0}/{1}", sutName, serviceName);
            String uriWithOutMode = ubiquoHost+secondPartUri+pathVariableUri;
            String definitiveUri = isIntegrationMode ?
                    uriWithOutMode+"/integration" :
                    uriWithOutMode+"/stubs";
            //OPTIONAL:
            if (registry.containsBeanDefinition(beanName)) {
                registry.removeBeanDefinition(beanName);
            }

            // Create the bean definition dynamically
            GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(WebClient.class);
            beanDefinition.setInstanceSupplier(() ->
                    WebClient.builder()
                            .baseUrl(definitiveUri)
                            .build()
            );
            beanDefinition.setPrimary(true); // optional if overriding by name
            //registry.removeBeanDefinition(beanName); // optional: clean existing
            registry.registerBeanDefinition(beanName, beanDefinition);
        }
    }
}
