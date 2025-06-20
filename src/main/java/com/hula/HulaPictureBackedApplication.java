package com.hula;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.hula.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableAsync
public class HulaPictureBackedApplication {
//加了一个注解
    public static void main(String[] args) {
        SpringApplication.run(HulaPictureBackedApplication.class, args);
    }

}
