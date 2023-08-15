package com.example.beanFactoryPostProcessor.base;

import com.alibaba.druid.pool.DruidDataSource;
import com.example.beanFactoryPostProcessor.component.Bean2;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author : KaelvihN
 * @date : 2023/8/13 19:20
 */

@ComponentScan("com.example.beanFactoryPostProcessor.component")
@Configuration
public class Config {

    public Bean2 bean2() {
        return new Bean2();
    }

    @Bean
    public Bean1 bean1() {
        return new Bean1();
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

    @Bean(initMethod = "init")
    public DataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://localhost:3306/kaelvihn_user");
        druidDataSource.setName("root");
        druidDataSource.setPassword("Mar.130118");
        return druidDataSource;
    }
}
