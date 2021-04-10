package com.yc;

import com.yc.bean.HelloWorld;
import com.yc.springframework.context.MyAnnotationConfigApplicationContext;
import com.yc.springframework.context.MyApplicationContext;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-05 12:06
 */
public class Test {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, ClassNotFoundException {

        MyApplicationContext ac=new MyAnnotationConfigApplicationContext(MyAppConfig.class);
        HelloWorld hw=(HelloWorld) ac.getBean("hw");
        hw.show();
    }
}
