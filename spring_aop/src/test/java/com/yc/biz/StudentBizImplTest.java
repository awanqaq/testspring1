package com.yc.biz;

import com.yc.MyAppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MyAppConfig.class)
public class StudentBizImplTest {
    @Autowired
    private StudentBiz sbi;

    @Test
    public void testAdd() {
    sbi.add("张三");
    }

    @Test
    public void testUpdate() {
        sbi.update("张三");
    }

    @Test
    public void testFind() throws InterruptedException {
        sbi.find("张三");
    }
}