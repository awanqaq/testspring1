package com.yc.biz;

import com.yc.dao.StudentDao;

/**
 * @program: testspring
 * @description: com.yc.biz
 * @author: ZYF
 * @create: 2021-04-05 09:05
 */
public class StudentBizImpl {
    private StudentDao studentDao;

    public StudentBizImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }
}
