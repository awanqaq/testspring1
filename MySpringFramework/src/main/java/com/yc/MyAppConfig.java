package com.yc;

import com.yc.bean.HelloWorld;
import com.yc.springframework.stereotype.MyBean;
import com.yc.springframework.stereotype.MyComponentScan;
import com.yc.springframework.stereotype.MyConfiguration;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-05 11:46
 */
@MyConfiguration
@MyComponentScan(basePackages = {"com.yc.bean"})
public class MyAppConfig {

    @MyBean
    public HelloWorld hw(){  //method.invoke(MyAppConfig对象)
        return new HelloWorld();
    }
    @MyBean
    public HelloWorld hw2(){  //method.invoke(MyAppConfig对象)
        return new HelloWorld();
    }
}
