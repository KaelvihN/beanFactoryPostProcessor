package com.example.beanFactoryPostProcessor;

import com.example.beanFactoryPostProcessor.base.Config;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.context.support.GenericApplicationContext;

import java.io.IOException;
import java.util.Arrays;


@Slf4j
public class BeanFactoryPostProcessorApplication {
    public static void main(String[] args) throws IOException {
        //创建一个“干净”的容器
        GenericApplicationContext context = new GenericApplicationContext();
        //注册Config类
        context.registerBean("config", Config.class);
//        /**
//         * 注册ConfigurationClassPostProcessor
//         * 用于解析(@Import,@ComponentScan,@Bean,@ComponentScan
//         */
//        context.registerBean(ConfigurationClassPostProcessor.class);
//        /**
//         *注册MapperScannerConfigurer，并制定要扫描的包
//         * @MapperScanner
//         */
//        context.registerBean(MapperScannerConfigurer.class, beanDefinition -> {
//            beanDefinition.getPropertyValues()
//                    .add("basePackage", "com/example/beanFactoryPostProcessor/mapper");
//        });
        /**
         * 模拟@ComponentScan
         */
        //查看Config类下是否有@ConponentScan注解,有则开始扫描
//        ComponentScan componentScan = AnnotationUtils.findAnnotation(Config.class, ComponentScan.class);
//        if (componentScan != null) {
//            //获取@ComponentScan 需要扫描的package
//            for (String basePackage : componentScan.basePackages()) {
//                //格式准换
//                //将com.example.beanFactoryPostProcessor.component
//                //转换为classpath*:com/example/beanFactoryPostProcessor/component/**/*.class
//                //**是指component包和component的所有子包
//                String path = "classpath*:" + basePackage.replace(".", "/") + "/**/*.class";
//                /**
//                 * CachingMetadataReaderFactory用于缓存类的元数据
//                 */
//                CachingMetadataReaderFactory factory = new CachingMetadataReaderFactory();
//                /**
//                 *Bean的接口申请器
//                 */
//                AnnotationBeanNameGenerator generator = new AnnotationBeanNameGenerator();
//                //读取path路径下所有的类
//                Resource[] resources = context.getResources(path);
//                for (Resource resource : resources) {
//                    //读取元数据
//                    MetadataReader reader = factory.getMetadataReader(resource);
//                    System.out.println("类名 = "+reader.getClassMetadata().getClassName());
//                    System.out.println("是否含有@Component注解 = "+reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
//                    System.out.println("是否含有@Component的派生注解 = "+reader.getAnnotationMetadata().hasMetaAnnotation(Component.class.getName()));
//                    //获取注解数据
//                    AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();
//                    //判断元数据中是否含有@Component或者其派生注解
//                    if (annotationMetadata.hasAnnotation(Component.class.getName()) ||
//                            annotationMetadata.hasMetaAnnotation(Component.class.getName())) {
//                        //生成Bean定义
//                        BeanDefinition beanDefinition =
//                                BeanDefinitionBuilder.genericBeanDefinition(reader.getClassMetadata().getClassName())
//                                        .getBeanDefinition();
//                        //将Bean定义加入到BeanFactory中
//                        DefaultListableBeanFactory beanFactory = context.getDefaultListableBeanFactory();
//                        String name = generator.generateBeanName(beanDefinition, beanFactory);
//                        beanFactory.registerBeanDefinition(name, beanDefinition);
//                    }
//                }
//            }
//        }


//        context.registerBean(ComponentScanPostProcessor.class);
        context.registerBean(AtBeanPostProcessor.class);
        context.registerBean(MapperPostProcessor.class);
        //初始化容器
        context.refresh();
        //遍历容器中的Bean
        Arrays.stream(context.getBeanDefinitionNames())
                .forEach(System.out::println);
        //销毁容器
        context.close();
    }
}
