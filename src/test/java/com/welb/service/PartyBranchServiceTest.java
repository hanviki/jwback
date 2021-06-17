package com.welb.service;

import com.welb.medicalEthics.entity.PartyBranchRelations;
import com.welb.medicalEthics.service.PartyBranchRelationsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 党支部查询
 * @author: luox
 * @date： 2020/11/20
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class PartyBranchServiceTest {

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Test
    public void testSelectById(){
        PartyBranchRelations partyBranchRelations = partyBranchRelationsService.selectById(65);
        System.out.println(partyBranchRelations);
    }

    /**
     * 测试列表条件查询
     */
    @Test
    public void testList(){
        Map<String,String> params = new HashMap<>();
        params.put("level","1");
        List<PartyBranchRelations> list = partyBranchRelationsService.list(params);
        System.out.println(list);
    }


}
