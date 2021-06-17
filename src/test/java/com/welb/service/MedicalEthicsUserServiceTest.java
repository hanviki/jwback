package com.welb.service;

import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 医德医风用户信息
 * @author: luox
 * @date： 2020/11/21
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class MedicalEthicsUserServiceTest {

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Test
    public void testSelectByParams(){
        Map<String,String> params = new HashMap<>();
        params.put("userId","10010508");
        List<MedicalEthicsUser> medicalEthicsUsers = medicalEthicsUserService.selectByParams(params);
        System.out.println(medicalEthicsUsers.size());
        System.out.println(medicalEthicsUsers);
    }

    @Test
    public void testDeleteByPrams(){
        Map<String,String> p = new HashMap<>();
        p.put("userId","10010931");
    }

}
