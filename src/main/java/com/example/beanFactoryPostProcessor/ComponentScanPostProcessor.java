package com.example.beanFactoryPostProcessor;

import com.example.beanFactoryPostProcessor.base.Config;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.stereotype.Component;

/**
 * @author : KaelvihN
 * @date : 2023/8/14 15:23
 */
public class ComponentScanPostProcessor implements BeanFactoryPostProcessor {


    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //检查Config类是否有@ComponentScan
        ComponentScan componentScan
                = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
        if (componentScan == null) {
            return;
        }

        //路径处理
        for (String basePackage : componentScan.basePackages()) {
            String path = "classpath*:" +
                    "" + basePackage.replace(".", "/")
                    + "/**" + "/*.class";
            //获取Resource
            Resource[] resources = new PathMatchingResourcePatternResolver().getResources(path);
            //缓存resource的元数据
            CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

            AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
            for (Resource resource : resources) {
                MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                //获取对应的注解的元数据
                AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                //判断annotationMetadata是否含有@Component或者Component的派生注解
                if (annotationMetadata.hasAnnotation(Component.class.getName()) ||
                        annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
                    //获取Bean定义
                    BeanDefinition beanDefinition =
                            BeanDefinitionBuilder.genericBeanDefinition(metadataReader.getClassMetadata().getClassName())
                                    .getBeanDefinition();
                    //获取Class的名字
                    if (beanFactory instanceof BeanDefinitionRegistry beanDefinitionRegistry) {
                        String name = generator.generateBeanName(beanDefinition, beanDefinitionRegistry);
                        beanDefinitionRegistry.registerBeanDefinition(name, beanDefinition);
                    }
                }
            }

        }
    }
}
