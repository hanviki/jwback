package com.welb.service;

import com.welb.medicalEthics.entity.EvaluationClinical;
import com.welb.medicalEthics.entity.PartyBranchRelations;
import com.welb.medicalEthics.service.EvaluationClinicalScoreService;
import com.welb.medicalEthics.service.EvaluationClinicalService;
import com.welb.medicalEthics.service.EvaluationNonClinicalService;
import com.welb.medicalEthics.entity.EvaluationNonClinical;
import com.welb.medicalEthics.service.PartyBranchRelationsService;
import com.welb.sysBase.entity.EvaluationDepartment;
import com.welb.sysBase.service.EvaluationDepartmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/11/17 13:51
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class EvaluationClinicalServiceTest {

    @Autowired
    private EvaluationNonClinicalService service;

    @Autowired
    private EvaluationClinicalScoreService scoreService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired
    private EvaluationDepartmentService evaluationDepartmentService;

    @Autowired
    private EvaluationClinicalService clinicalService;

    /**
     * testInit 初始化非临床人员表单
     *
     * @date 2020/11/17
     */
    @Test
    public void testInit(){
        try {
            EvaluationNonClinical evaluationNonClinical = service.initInfo("10010316");
            System.out.println(evaluationNonClinical);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInsert(){
        EvaluationNonClinical newEvaluationNonClinical = new EvaluationNonClinical();
        newEvaluationNonClinical.setId(122);
//        newEvaluationNonClinical.setUsername("2132132132");
//        newEvaluationNonClinical.setUserId("232132132132");
        //service.insert(newEvaluationNonClinical);
    }

    @Test
    public void testUpdateById(){
        /*EvaluationNonClinical evaluationNonClinical = service.selectByUserId("10010316");
        evaluationNonClinical.setEducationLevel("研究生");
        service.updateById(evaluationNonClinical);*/
    }

    @Test
    public void testGenerateFormItem(){
        Boolean aBoolean = scoreService.generateFormItem("10011168");
        System.out.println(aBoolean);
    }

    @Test
    public void testGetTree(){
        Map<String,String> params = new HashMap<>();
        params.put("parentId","1");
        List<PartyBranchRelations> list = partyBranchRelationsService.list(null);
        /*转为json看看效果*/
        System.out.println(true);
    }

    @Test
    public void testSelectByParams(){
        Map<String,String> params = new HashMap<>();
        params.put("userId","10031632");
        params.put("year","2020");
        List<EvaluationNonClinical> list = service.list(params);
        System.out.println(list);
    }

    @Test
    public void testSelectList(){
        Map<String,String> params = new HashMap<>();
        params.put("departmentName","神经外科");
        List<EvaluationDepartment> list = evaluationDepartmentService.list(params);
        System.out.println(list);
    }

    @Test
    public void testUpdateScoreInfo(){
        Map<String, String> params = new HashMap<>();
        params.put("userId", "10010213");
        params.put("year", "2020");
        //EvaluationClinical clinical = clinicalService.list(params).get(0);
//        clinical.setScore(70);
        //clinical.setLevel(3);
        //clinicalService.updateById(clinical);
    }

}
