package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.entity.EvaluationNonClinical;
import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.PartyBranchRelations;
import com.welb.medicalEthics.mapper.EvaluationNonClinicalMapper;
import com.welb.medicalEthics.mapper.PartyBranchRelationsMapper;
import com.welb.medicalEthics.service.*;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.Constant;
import com.welb.util.DateUtil;
import com.welb.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.welb.sysBase.util.Constant.*;

/**
 * @description: 非临床医护人员考核
 * @author: luox
 * @date： 2020/11/17 10:43
 */

@Service
public class EvaluationNonClinicalServiceImpl implements EvaluationNonClinicalService {

    @Autowired
    private EvaluationNonClinicalMapper evaluationNonClinicalMapper;

    @Autowired
    private HUserService personneService;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired(required = false)
    private PartyBranchRelationsMapper partyBranchRelationsMapper;

    @Autowired
    private MedicalEthicsDicService dicService;

    @Autowired
    private MedicalEthicsMsgService msgService;

    @Override
    public Integer insert(EvaluationNonClinical evaluationNonClinical) {
        return evaluationNonClinicalMapper.insert(evaluationNonClinical);
    }

    @Override
    public void updateById(EvaluationNonClinical evaluationNonClinical) {
        evaluationNonClinicalMapper.updateById(evaluationNonClinical);
    }


    @Override
    public EvaluationNonClinical selectByUserId(String userId) {
        EvaluationNonClinical nonClinical = evaluationNonClinicalMapper.selectByUserId(userId);
        if(nonClinical != null){
            String politicalStatus = dicService.selectByCodeAndType(DIC_TYPE_POLITICAL_STATUS, nonClinical.getPoliticalStatus());
            String educationLevel =  dicService.selectByCodeAndType(DIC_TYPE_EDUCATION_LEVEL, nonClinical.getEducationLevel());
            String title =  dicService.selectByCodeAndType(DIC_TYPE_TITLE, nonClinical.getTitle());
            nonClinical.setPoliticalStatusName(politicalStatus);
            nonClinical.setEducationLevelName(educationLevel);
            nonClinical.setTitleName(title);
        }
        return nonClinical;
    }

    /**
     * 页面初始化创建一个空表单
     *
     * @param userId
     */
    @Override
    public EvaluationNonClinical initInfo(String userId) {
        //查询表单是否存在
        EvaluationNonClinical evaluationNonClinical = selectByUserId(userId);
        if (null == evaluationNonClinical) {
            MedicalEthicsMsg user = msgService.selectByUserId(userId);
            if (null != user) {
                //考核用户存在新建表单
                EvaluationNonClinical newEvaluationNonClinical = new EvaluationNonClinical();
                PersonnelUser personnelUser = personneService.selectByUserId(userId);
                //获取用户生日等基本信息
                newEvaluationNonClinical.setUsername(personnelUser.getUsername());
                newEvaluationNonClinical.setSex(personnelUser.getSex().equals(SEX_MALE) ? STATUS_ONE : STATUS_ZERO);
                newEvaluationNonClinical.setBirth(personnelUser.getBirth());

                //获取年份
                newEvaluationNonClinical.setYear(CURRENT_YEAR);
                newEvaluationNonClinical.setUserId(userId);
                newEvaluationNonClinical.setStep(Constant.EVALUATION_NON_CLINICAL_DEFAULT);
                //设置党支部信息
                newEvaluationNonClinical.setBranchId(user.getBranchId());
                newEvaluationNonClinical.setGeneralBranchId(user.getGeneralBranchId());
                newEvaluationNonClinical.setPartyCommitteesId(user.getPartyCommitteesId());
                //插入表单
                insert(newEvaluationNonClinical);
                return selectByUserId(userId);
            } else {
                //用户信息为空
                return null;
            }
        } else {
            //已经存在了表单
            return evaluationNonClinical;
        }
    }

    /**
     * updateForm 提交表单操作
     *
     * @param evaluationNonClinical 表单信息
     * @return {@link Integer}
     */
    @Transactional
    @Override
    public Integer updateForm(EvaluationNonClinical evaluationNonClinical, String userName) {
        int step = evaluationNonClinical.getStep();
        //增加操作人和时间
        this.addSubmitMsg(evaluationNonClinical, userName, step);
        if (StringUtils.isNotEmpty(evaluationNonClinical.getSelfSummary()) && Constant.EVALUATION_NON_CLINICAL_DEFAULT == step) {
            //填写基本信息和自我总结
            MedicalEthicsUser ethicsUser = medicalEthicsUserService.selectByUserId(evaluationNonClinical.getUserId()).get(0);
            //同步学历工作等信息到基本信息表
            if (ethicsUser != null) {
                if (evaluationNonClinical.getSex() != null) {
                    ethicsUser.setSex(evaluationNonClinical.getSex());
                }
                if (evaluationNonClinical.getUserId() != null) {
                    ethicsUser.setUserId(evaluationNonClinical.getUserId());
                }
                if (evaluationNonClinical.getPoliticalStatus() != null) {
                    ethicsUser.setPoliticalStatus(evaluationNonClinical.getPoliticalStatus());
                }
                if (evaluationNonClinical.getEducationLevel() != null) {
                    ethicsUser.setEducationLevel((evaluationNonClinical.getEducationLevel()));
                }
                if (evaluationNonClinical.getTitle() != null) {
                    ethicsUser.setTitle(evaluationNonClinical.getTitle());
                }
                if (evaluationNonClinical.getCurrentPosition() != null) {
                    ethicsUser.setCurrentPosition(evaluationNonClinical.getCurrentPosition());
                }
                if (evaluationNonClinical.getHireDate() != null) {
                    ethicsUser.setHireDate(evaluationNonClinical.getHireDate());
                }
                if (evaluationNonClinical.getJobContent() != null) {
                    ethicsUser.setJobContent(evaluationNonClinical.getJobContent());
                }
                //同步更新人员基本信息
                Integer isTemp = evaluationNonClinical.getIsTemp();
                if(isTemp != null && isTemp == 1){
                    evaluationNonClinical.setStep(Constant.EVALUATION_NON_CLINICAL_DEFAULT);
                }else{
                    evaluationNonClinical.setStep(Constant.EVALUATION_NON_CLINICAL_SELF_SUMMARY);
                }
                //更新表单信息
                try {
                    medicalEthicsUserService.updateBaseInfo(ethicsUser);
                    updateById(evaluationNonClinical);
                    return Constant.EVALUATION_CLINICAL_SUCCESS;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                return Constant.EVALUATION_CLINICAL_FAIL;
            }
        } else if (evaluationNonClinical.getDeptHeadOpinion() != null && Constant.EVALUATION_NON_CLINICAL_SELF_SUMMARY == step) {
            //部门领导意见
            evaluationNonClinical.setStep(Constant.EVALUATION_NON_CLINICAL_DEPT_HEAD_OPINION);
            evaluationNonClinical.setDeptHeadOpinion(evaluationNonClinical.getDeptHeadOpinion());
            updateById(evaluationNonClinical);
            return Constant.EVALUATION_CLINICAL_SUCCESS;
        } else if (evaluationNonClinical.getBranchOpinion() != null && Constant.EVALUATION_NON_CLINICAL_DEPT_HEAD_OPINION == step) {
            //党委意见
            if (evaluationNonClinical.getBranchOpinion() == 1) {
                Map<String, Object> map = checkExcellentPercent(evaluationNonClinical.getBranchId());
                    String data = map.get("data").toString();
                    // 更新当前的优秀比例
                    PartyBranchRelations relations = partyBranchRelationsService.selectById(evaluationNonClinical.getBranchId());
                    relations.setCurrentExcellentPercent(Double.parseDouble(data));
                    partyBranchRelationsMapper.update(relations);
            }
            //更新的进度
            evaluationNonClinical.setStep(Constant.EVALUATION_NON_CLINICAL_BRANCH_OPINION);
            evaluationNonClinical.setLevel(evaluationNonClinical.getBranchOpinion());
            updateById(evaluationNonClinical);
            return Constant.EVALUATION_CLINICAL_SUCCESS;
        }
        return Constant.EVALUATION_CLINICAL_FAIL;
    }

    /**
     * 书记&主任重新编辑
     * @param evaluationNonClinical 非临床
     * @param userName 打分人
     * @param roleType 角色类型
     */
    @Override
    public Integer editForm(EvaluationNonClinical evaluationNonClinical, String userName,String roleType) {
        try {
            if(ROLE_ID_DIRECTOR.equals(roleType)){
                evaluationNonClinical.setHeadSubmitName(userName);
                evaluationNonClinical.setHeadSubmitTime(DateUtil.getTime());
            }else if(ROLE_ID_SCORE_DIC.equals(roleType)){
                evaluationNonClinical.setBranchSubmitName(userName);
                evaluationNonClinical.setBranchSubmitTime(DateUtil.getTime());
            }
            //更新评分等级(优秀/良好/一般/较差)
            evaluationNonClinical.setLevel(evaluationNonClinical.getBranchOpinion().intValue());
            updateById(evaluationNonClinical);
            return Constant.EVALUATION_CLINICAL_SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return Constant.EVALUATION_CLINICAL_FAIL;
        }
    }

    @Override
    public Integer getCountByParams(Map<String, String> params) {
        return evaluationNonClinicalMapper.selectCountByParams(params);
    }

    @Override
    public List<EvaluationNonClinical> list(Map<String, String> prams) {
        return evaluationNonClinicalMapper.list(prams);
    }

    /**
     * 如果得分为优秀,检查优秀率是否超标,未超标返回优秀率
     */
    Map<String, Object> checkExcellentPercent(Integer branchId) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("id", String.valueOf(branchId));
        List<PartyBranchRelations> list = partyBranchRelationsService.list(queryParams);
        if (!list.isEmpty()) {
            PartyBranchRelations res = list.get(0);
            Map<String, String> map = new HashMap<>();
            map.put("branchOpinion", "1");
            Integer totalCount = getCountByParams(null);
            Integer excellentCount = getCountByParams(map) + 1;

            Double dTotal = Double.valueOf(totalCount);
            Double dExcellent = Double.valueOf(excellentCount);
            //返回新的优秀率
            double v = dExcellent / dTotal * 100;
//            Boolean isValid = v < res.getExcellentPercent();
//            if (isValid) {
                resultMap.put("result", "true");
                resultMap.put("data", v);
                return resultMap;
//            }
        }
        return resultMap;
    }

    /**
     * 扩展党支部名字返还给前端
     * @param list
     * @return
     */
    @Override
    public List<EvaluationNonClinical> extendPartyInfo(List<EvaluationNonClinical> list){
        List<EvaluationNonClinical> voList =new ArrayList<>();
        list.forEach(data ->{
            Integer partyCommitteesId = data.getPartyCommitteesId();
            Integer generalBranchId = data.getGeneralBranchId();
            Integer branchId = data.getBranchId();
            data.setPartyCommitteesName(partyBranchRelationsService.selectById(partyCommitteesId).getRelationsName());
            data.setGeneralBranchName(partyBranchRelationsService.selectById(generalBranchId).getRelationsName());
            data.setBranchName(partyBranchRelationsService.selectById(branchId).getRelationsName());
            voList.add(data);
        });
        return voList;
    }

    @Transactional
    @Override
    public void updateStepByUserId(String userId) {
        //查询当前打分步骤
        EvaluationNonClinical curNonClinical = evaluationNonClinicalMapper.list(new HashMap<String, String>(){{put("userId", userId); put("year", String.valueOf(LocalDate.now().getYear()));}}).get(0);
        int curStep = curNonClinical.getStep();
        //回退步骤
        int finalStep = curStep - 1;
        evaluationNonClinicalMapper.updateStepByUserId(finalStep, userId);
    }

    @Override
    public void deleteByUserId(String userId) {
        evaluationNonClinicalMapper.deleteByUserId(userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchFinish(List<String> userIdList, int step) {
        evaluationNonClinicalMapper.batchFinish(userIdList, step);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void finishAll(int step) {
        evaluationNonClinicalMapper.finishAll(step);
    }

    private void addSubmitMsg(EvaluationNonClinical evaluationNonClinical, String userName, int step) {
        switch (step){
            case SELF_SUBMIT:
                //增加自我评分提交信息
                evaluationNonClinical.setSelfSubmitName(userName);
                evaluationNonClinical.setSelfSubmitTime(DateUtils.getNowTime());
                break;
            case DIRECTOR_SUBMIT:
                //增加主任评分提交信息
                evaluationNonClinical.setHeadSubmitName(userName);
                evaluationNonClinical.setHeadSubmitTime(DateUtils.getNowTime());
                break;
            case SECRETARY_SUBMIT:
                //增加书记评分提交信息
                evaluationNonClinical.setBranchSubmitName(userName);
                evaluationNonClinical.setBranchSubmitTime(DateUtils.getNowTime());
                break;
            }
    }
}
