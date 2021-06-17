package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.vo.CheckResultVo;
import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.entity.EvaluationDepartmentUser;
import com.welb.sysBase.mapper.EvaluationDepartmentMapper;
import com.welb.sysBase.mapper.EvaluationDepartmentUserMapper;
import com.welb.sysBase.service.BranchDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/7
 */

@Service
public class EvaluationDepartmentUserServiceImpl implements EvaluationDepartmentUserService {

    @Autowired
    private EvaluationDepartmentUserMapper departmentUserMapper;

    @Autowired
    private EvaluationDepartmentMapper departmentMapper;

    @Autowired
    private BranchDepartmentService branchDepartmentService;

    @Override
    public void insert(EvaluationDepartmentUser departmentUser) {
        departmentUserMapper.insert(departmentUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<EvaluationDepartmentUser> list) {
        departmentUserMapper.batchInsert(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<String> ids) {
        departmentUserMapper.batchDelete(ids);
    }

    @Override
    public void deleteByParams(Map<String, String> params) {
        departmentUserMapper.deleteByParams(params);
    }

    @Override
    public List<EvaluationDepartmentUser> selectByUserId(Integer userId) {
        return departmentUserMapper.selectByUserId(userId);
    }

    @Override
    public List<EvaluationDepartmentUser> selectByDepartmentId(Integer departmentId) {
        return departmentUserMapper.selectByDepartmentId(departmentId);
    }

    @Override
    public List<DepartmentUserVo> selectUserDetailByDepartmentId(Map<String,String> params){
        return departmentUserMapper.selectUserDetailByDepartmentId(params);
    }

    @Override
    public List<DepartmentUserVo> selectUserDetailByBranchId(Map<String,String> params){
        return departmentUserMapper.selectUserDetailByBranchId(params);
    }

    @Override
    public List<DepartmentUserVo> selectUserDetailByParams(Map<String, Object> params) {
        return  departmentUserMapper.selectUserDetailByParams(params);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoDepartment(List<MedicalEthicsMsg> medicalEthicsMsgList) {
        for(MedicalEthicsMsg msg : medicalEthicsMsgList){
            int branchId = msg.getBranchId();
            List<BranchDepartment> branchDepartments = branchDepartmentService.selectByBranchId(branchId);
            if(branchDepartments.size() == 1){
                BranchDepartment branchDepartment = branchDepartments.get(0);
                Integer departmentId = branchDepartment.getDepartmentId();
                EvaluationDepartmentUser departmentUser = new EvaluationDepartmentUser();
                Map<String,String> params = new HashMap<>();
                params.put("userId",msg.getUserId());
                departmentUserMapper.deleteByParams(params);
                departmentUser.setUserId(Integer.valueOf(msg.getUserId()));
                departmentUser.setDepartmentId(departmentId);
                departmentUserMapper.insert(departmentUser);
            }
        }
    }

    @Override
    public List<MedicalEthicsMsg> selectMsgList(Map<String, String> params) {
        return departmentUserMapper.selectMsgList(params);
    }

    /**
     * 查询所有的信息
     */
    @Override
    public List<CheckResultVo> selectCheckDetailByParams(Map<String, Object> params) {
        return departmentUserMapper.selectCheckDetailByParams(params);
    }

    /**
     * 用户科室绑定
     * @param departmentUser
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindNewUser(EvaluationDepartmentUser departmentUser) {
        Map<String,String> deleteParams = new HashMap<>();
        deleteParams.put("userId",departmentUser.getUserId().toString());
        departmentUserMapper.deleteByParams(deleteParams);
        departmentUserMapper.insert(departmentUser);
    }

    @Override
    public List<CheckResultVo> checkResultByType(Map<String, Object> params) {
        return departmentUserMapper.checkResultByType(params);
    }

}
