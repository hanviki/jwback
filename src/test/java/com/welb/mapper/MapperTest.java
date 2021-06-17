package com.welb.mapper;

import com.welb.medicalEthics.mapper.EvaluationNonClinicalMapper;
import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.mapper.BranchDepartmentMapper;
import com.welb.sysBase.service.EvaluationDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/11/25
 */

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class MapperTest {

    @Autowired
    private EvaluationNonClinicalMapper evaluationNonClinicalMapper;

    @Autowired
    private BranchDepartmentMapper branchDepartmentMapper;

    @Autowired
    EvaluationDepartmentUserService departmentUserService;

    @Test
    public void testSelectCountByParams(){
        Map<String,String> params = new HashMap<>();
        params.put("branchOpinion","1");
        Integer count = evaluationNonClinicalMapper.selectCountByParams(params);
        System.out.println(count);
    }

    @Test
    public void testInsert(){
        BranchDepartment department = new BranchDepartment();
        department.setDepartmentId(213213213);
        department.setBranchId(23213213);
        branchDepartmentMapper.insert(department);
    }

    @Test
    public void getData(){
        List<Integer> branchIdList= new ArrayList<>();
        branchIdList.add(120);
        branchIdList.add(309);
        Map<String,Object> s = new HashMap<>();
        s.put("branchIdList",branchIdList);
        //List<DepartmentUserVo> departmentUserVos = departmentUserService.selectUserDetailByParams(s);
        //System.out.println(departmentUserVos);
    }

}
