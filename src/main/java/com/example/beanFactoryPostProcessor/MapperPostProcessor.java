package com.example.beanFactoryPostProcessor;

import lombok.SneakyThrows;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

/**
 * @author : KaelvihN
 * @date : 2023/8/15 12:45
 */
public class MapperPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @SneakyThrows
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        //读取mapper包下的类
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources =
                resolver.getResources("classpath:com/example/beanFactoryPostProcessor/mapper/**/*.class");
        CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
        AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
        for (Resource resource : resources) {
            //获取每个类的元数据
            MetadataReader metadataReader = factory.getMetadataReader(resource);
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            //判断是否为接口
            if (classMetadata.isInterface()&&annotationMetadata.hasAnnotation(Mapper.class.getName())) {
//                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class);
//                beanDefinitionBuilder.addConstructorArgValue(classMetadata.getClassName());
//                beanDefinitionBuilder.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//               BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                //定义BeanDefinition
                BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(MapperFactoryBean.class)
                        .addConstructorArgValue(classMetadata.getClassName())
                        .setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE)
                        .getBeanDefinition();
                AbstractBeanDefinition bd =
                        BeanDefinitionBuilder.genericBeanDefinition(classMetadata.getClassName())
                                .getBeanDefinition();
                String name = generator.generateBeanName(bd, registry);
                registry.registerBeanDefinition(name, beanDefinition);
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
