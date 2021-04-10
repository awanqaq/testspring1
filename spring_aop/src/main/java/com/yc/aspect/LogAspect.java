package com.yc.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-09 20:26
 */
@Aspect  //切面类：要增强的功能写到这里
@Component //IOC注解 实现让spring托管的功能
public class LogAspect {

    @Pointcut("execution(* com.yc.biz.StudentBizImpl.add*(..))") //切入表达式：哪些方法上增强
    private void add(){

    }
    @Pointcut("execution(* com.yc.biz.StudentBizImpl.update*(..))") //切入表达式：哪些方法上增强
    private void update(){

    }

    @Pointcut("add()||update()")
    private  void  addAndUpdate(){

    }
    //切入点表达式的语法：  ？代表出现0次或1次

    @Before("com.yc.aspect.LogAspect.add()")
    public void log(){
        System.out.println("==============前置增强的日志===============");
        Date d=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dstr=sdf.format(d);
        System.out.println("执行时间为："+dstr);
        System.out.println("=============前置增强的日志结束=============");

    }
}
