package com.yc.dao;

import org.springframework.stereotype.Repository;

import java.util.Random;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-12 20:06
 */
@Repository
public class StudentDaoJpaImpl implements StudentDao{
    public int add(String name) {
        System.out.println("jpa添加学生："+ name);
        Random r=new Random();
        return r.nextInt();
    }

    public void update(String name) {
        System.out.println("jpa更新学生:"+name);
    }

    public String find(String name) {
        System.out.println("jpa查找："+name);
        return name;
    }
}
