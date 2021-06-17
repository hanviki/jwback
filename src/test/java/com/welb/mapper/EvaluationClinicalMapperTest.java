package com.welb.mapper;

import com.welb.medicalEthics.entity.EvaluationClinicalScore;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.mapper.EvaluationClinicalScoreMapper;
import com.welb.medicalEthics.mapper.MedicalEthicsUserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author: luox
 * @date： 2020/11/18 11:37
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class EvaluationClinicalMapperTest {

    @Autowired
    private MedicalEthicsUserMapper medicalEthicsUserMapper;

    @Autowired
    private EvaluationClinicalScoreMapper scoreMapper;

    @Test
    public void testSelectByUserId(){
//        MedicalEthicsUser medicalEthicsUser = medicalEthicsUserMapper.selectByUserId("10010954");
//        System.out.println(medicalEthicsUser);
    }

    @Test
    public void testBatchInsert(){
//        List<EvaluationClinicalScore> res = scoreMapper.selectByParams("123", null);
//        System.out.println(res);
    }

}
