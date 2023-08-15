package com.example.beanFactoryPostProcessor;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * @author : KaelvihN
 * @date : 2023/8/15 0:04
 */
public class AtBeanPostProcessor implements BeanFactoryPostProcessor {
    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //获取Config的元数据
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        MetadataReader metadataReader =
                factory.getMetadataReader(
                        new ClassPathResource("com/example/beanFactoryPostProcessor/base/Config.class"));
        //获取Config中含有@Bean注解的方法,并封装成MethodMetadata保存在Set集合里
        Set<MethodMetadata> methods = metadataReader.getAnnotationMetadata()
                .getAnnotatedMethods(Bean.class.getName());
        //遍历集合
        for (MethodMetadata method : methods) {
            System.out.println("method = " + method);
            /**
             * 获取@Bean中,"initMethod"的属性值
             */
            String initMethod = method.getAnnotationAttributes(Bean.class.getName())
                    .get("initMethod")
                    .toString();

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition();
            builder.setFactoryMethodOnBean(method.getMethodName(), "config");
            /**
             * 自动装配模式
             */
            builder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);
            /**
             * @Bean属性解析
             */
            if (StringUtils.hasLength(initMethod)) {
                builder.setInitMethodName(initMethod);
            }
            //获取Bean定义
            BeanDefinition beanDefinition = builder.getBeanDefinition();
            //注册Bean定义
            if (beanFactory instanceof BeanDefinitionRegistry beanDefinitionRegistry) {
                beanDefinitionRegistry.registerBeanDefinition(method.getMethodName(), beanDefinition);
            }
        }
    }
}
