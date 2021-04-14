package com.yc.bean;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-12 20:20
 */
@Component
public class HelloWorld {
    @PostConstruct
    public void setup(){
        System.out.println("MyPostConstruct");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("MyPreDestroy");
    }

    public HelloWorld(){
        System.out.println("hello world 构造");
    }

    public void show(){
        System.out.println("show");
    }
}
