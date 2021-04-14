package com.yc.biz;

import com.yc.LogAspectCglib;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-14 11:12
 */
public class Test {
    public static void main(String[] args) {
        StudentBizImpl sbi=new StudentBizImpl();

        LogAspectCglib lc=new LogAspectCglib(sbi);

        //生成代理类对象
        Object obj=lc.createProxy();
        System.out.println(obj); //obj.toString()
        if(obj instanceof StudentBizImpl){
            StudentBizImpl s=(StudentBizImpl)obj;
            s.find("张三");
            s.update("李四");
            s.add("王五");
        }

    }
}
