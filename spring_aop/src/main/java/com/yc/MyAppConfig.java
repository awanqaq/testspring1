package com.yc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-09 20:33
 */
@Configuration
@ComponentScan(basePackages = {"com.yc"})
@EnableAspectJAutoProxy //启用AspectJ支持
public class MyAppConfig {
}
