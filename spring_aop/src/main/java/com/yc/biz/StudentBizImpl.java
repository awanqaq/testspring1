package com.yc.biz;

import com.yc.dao.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @program: testspring
 * @description:
 * @author: ZYF
 * @create: 2021-04-09 20:35
 */
@Service
public class StudentBizImpl implements StudentBiz {
    private StudentDao studentDao;

    public StudentBizImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public StudentBizImpl() {

    }

    @Autowired
    @Qualifier("studentDaoJpaImpl")
    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    public int add(String name) {
        System.out.println("------业务层---------");
        System.out.println("用户名是否重名");
        int result = studentDao.add(name);
        System.out.println("业务操作结束");
        return result;
    }

    public void update(String name) {
        System.out.println("=======业务层======");
        System.out.println("用户名是否重名");
        studentDao.update(name);
        System.out.println("业务层结束");
    }

    public void find(String name) throws InterruptedException {
        System.out.println("=======业务层======");
        System.out.println("业务层查找用户名" + name);
        studentDao.find(name);
        Random r=new Random();
        Thread.sleep(r.nextInt(10));
        System.out.println("业务层结束");
    }

}
