package com.welb.service;

import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.sysBase.service.HUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: luox
 * @date： 2020/11/17 13:28
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class HUserviceTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    HUserService personneService;

    @Test
    public void testSelectByUserId(){
        PersonnelUser personnelUser = personneService.selectByUserId("10010002");
        System.out.println(personnelUser);
    }

}
