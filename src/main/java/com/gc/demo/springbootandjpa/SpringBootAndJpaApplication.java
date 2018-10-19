package com.gc.demo.springbootandjpa;

import com.gc.demo.springbootandjpa.utils.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
//@EntityScan(basePackages={"com.gc.demo.springbootandjpa.entity"})//实体在其他包下时加这个注解
@EnableJpaRepositories(basePackages = {"com.gc.demo.springbootandjpa"}, repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)//指定自己的工厂类
public class SpringBootAndJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootAndJpaApplication.class, args);
    }
}
