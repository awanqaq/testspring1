package com.yc.dao;

import java.util.Random;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-12 20:06
 */
public class StudentDaoMybatisImpl implements StudentDao{
    public int add(String name) {
        System.out.println("mybatis添加学生："+name);
        Random r=new Random();
        return r.nextInt();
    }

    public void update(String name) {
        System.out.println("mybatis更新学生:"+name);
    }

    public String find(String name) {
        System.out.println("mybatis查找："+name);
        return name;
    }
}
