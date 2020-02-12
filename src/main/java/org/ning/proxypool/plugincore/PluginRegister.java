package org.ning.proxypool.plugincore;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

/**
 * @author: NingWang
 * @since 2020/2/11.
 */
@Slf4j
public class PluginRegister implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        registerBeanDefinitionIfNotExists(registry, PluginBeanPostProcessor.class, null);
        registerBeanDefinitionIfNotExists(registry, PluginBootstrapListener.class, null);
    }

    private boolean registerBeanDefinitionIfNotExists(BeanDefinitionRegistry registry, Class<?> beanClass, Object... args) {
        if (registry.containsBeanDefinition(beanClass.getName())) {
            return false;
        }
        String[] candidates = registry.getBeanDefinitionNames();
        for (String candidate : candidates) {
            BeanDefinition beanDefinition = registry.getBeanDefinition(candidate);
            if (Objects.equals(beanDefinition.getBeanClassName(), beanClass.getName())) {
                return false;
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                beanDefinitionBuilder.addConstructorArgValue(arg);
            }
        }
        BeanDefinition annotationProcessor = beanDefinitionBuilder.getBeanDefinition();
        registry.registerBeanDefinition(beanClass.getName(), annotationProcessor);
        return true;
    }

}
