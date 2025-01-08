package com.hula;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.hula.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class HulaPictureBackedApplication {

    public static void main(String[] args) {
        SpringApplication.run(HulaPictureBackedApplication.class, args);
    }

}
