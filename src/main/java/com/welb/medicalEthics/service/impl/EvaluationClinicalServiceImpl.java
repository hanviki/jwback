package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.dto.ScoreDto;
import com.welb.medicalEthics.entity.EvaluationClinical;
import com.welb.medicalEthics.entity.EvaluationClinicalScore;
import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.mapper.EvaluationClinicalMapper;
import com.welb.medicalEthics.mapper.EvaluationNonClinicalMapper;
import com.welb.medicalEthics.service.*;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.Constant;
import com.welb.util.DateUtil;
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
 * @description: 临床工作人员考核
 * @author: luox
 * @date： 2020/11/18 15:04
 */

@Service
public class EvaluationClinicalServiceImpl implements EvaluationClinicalService {

    @Autowired
    private EvaluationClinicalMapper evaluationClinicalMapper;

    @Autowired
    private EvaluationNonClinicalMapper nonClinicalMapper;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired
    private HUserService personneService;

    @Autowired
    private EvaluationClinicalScoreService scoreService;

    @Autowired
    private MedicalEthicsMsgService msgService;

    /**
     * 创建表单
     *
     * @param evaluationClinical 表单实体
     * @return
     */
    @Override
    public Integer insert(EvaluationClinical evaluationClinical) {
        return evaluationClinicalMapper.insert(evaluationClinical);
    }

    @Override
    @Transactional
    public EvaluationClinical initInfo(String userId) {
        EvaluationClinical evaluationClinical = selectByUserId(userId);
        //尚未创建过表单,创建新表单
        if (null == evaluationClinical) {
            MedicalEthicsMsg user = msgService.selectByUserId(userId);
            if (null != user) {
                EvaluationClinical newEvaluationClinical = new EvaluationClinical();
                //获取用户生日等基本信息
                PersonnelUser personnelUser = personneService.selectByUserId(userId);
                if(personnelUser != null){
                    newEvaluationClinical.setUsername(personnelUser.getUsername());
                    newEvaluationClinical.setSex(personnelUser.getSex().equals(SEX_MALE) ? STATUS_ONE : STATUS_ZERO);
                    newEvaluationClinical.setBirth(personnelUser.getBirth());
                    //设置年份
                    newEvaluationClinical.setYear(CURRENT_YEAR);
                    newEvaluationClinical.setUserId(user.getUserId());
                    newEvaluationClinical.setStep(0);
                    //设置党支部信息
                    newEvaluationClinical.setBranchId(user.getBranchId());
                    newEvaluationClinical.setGeneralBranchId(user.getGeneralBranchId());
                    newEvaluationClinical.setPartyCommitteesId(user.getPartyCommitteesId());
                    //创建新的表单
                    insert(newEvaluationClinical);
                    return selectByUserId(userId);
                }else{
                    System.out.println("--------------------------------------------------------------------------------"+userId);
                    return null;
                }

            } else {
                return null;
            }
        } else {
            //已经创建过,返回已有的表单
            return evaluationClinical;
        }
    }

    /**
     * 根据userId 获取表单
     *
     * @param userId
     * @return
     */
    @Override
    public EvaluationClinical selectByUserId(String userId) {
        return evaluationClinicalMapper.selectByUserId(userId);
    }

    /**
     * 提交表单基本信息
     *
     * @param evaluationClinical
     * @return
     */
    @Override
    public Boolean updateBaseInfo(EvaluationClinical evaluationClinical) {
        String userId = evaluationClinical.getUserId();
        EvaluationClinical clinical = selectByUserId(userId);
        Integer step = clinical.getStep();

        if (Constant.EVALUATION_CLINICAL_DEFAULT.equals(step)) {
            //填写基本信息
            MedicalEthicsUser ethicsUser = medicalEthicsUserService.selectByUserId(userId).get(0);
            if (ethicsUser != null) {
                //设置进度
                evaluationClinical.setStep(Constant.EVALUATION_CLINICAL_BASE_INFO);
                try {
                    //同步更新到基本信息表
                    medicalEthicsUserService.updateBaseInfo(ethicsUser);
                    updateById(evaluationClinical);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        } else if (StringUtils.isNotEmpty(evaluationClinical.getSelfSummary()) && Constant.EVALUATION_NON_CLINICAL_SELF_SUMMARY.equals(step)) {
            evaluationClinical.setStep(Constant.EVALUATION_CLINICAL_SELF_SUMMARY);
            updateById(evaluationClinical);
            return true;
        }
        return false;
    }

    /**
     * updateSelfSummary 自我总结 步骤码变为2
     *
     * @param userId      userId
     * @param selfSummary selfSummary
     */
    @Override
    public void updateSelfSummary(String userId, String selfSummary) {
        EvaluationClinical evaluationClinical = selectByUserId(userId);
        evaluationClinical.setSelfSummary(selfSummary);
        evaluationClinical.setStep(2);
        updateByUserId(evaluationClinical);
    }

    @Override
    public void updateSelfSummaryTemp(String userId, String selfSummary) {
        //暂存不更新step
        EvaluationClinical evaluationClinical = selectByUserId(userId);
        evaluationClinical.setSelfSummary(selfSummary);
        updateByUserId(evaluationClinical);
    }

    @Override
    public void updateByUserId(EvaluationClinical evaluationClinical) {
        evaluationClinicalMapper.updateByUserId(evaluationClinical);
    }

    @Override
    public List<EvaluationClinical> list(Map<String, String> params) {
        return evaluationClinicalMapper.list(params);
    }

    /**
     * extendPartyInfo 扩展信息,根据支部id获取支部名字返还前端
     *
     * @param list list
     * @return {@link List<EvaluationClinical>}
     */
    @Override
    public List<EvaluationClinical> extendPartyInfo(List<EvaluationClinical> list) {

        if (!list.isEmpty()) {
            String userId = list.get(0).getUserId();
            List<EvaluationClinical> voList = new ArrayList<>();
            list.forEach(data -> {

                Integer partyCommitteesId = data.getPartyCommitteesId();
                Integer generalBranchId = data.getGeneralBranchId();
                Integer branchId = data.getBranchId();
                data.setPartyCommitteesName(partyBranchRelationsService.selectById(partyCommitteesId).getRelationsName());
                data.setGeneralBranchName(partyBranchRelationsService.selectById(generalBranchId).getRelationsName());
                data.setBranchName(partyBranchRelationsService.selectById(branchId).getRelationsName());
                if (data.getStep() > 2) {
                    ScoreDto calculate = scoreService.calculate(userId, data.getYear());
                    data.setTotalSelfScore(calculate.getTotalSelfScore());
                    data.setTotalHeadScore(calculate.getTotalHeadScore());
                    data.setTotalBranchScore(calculate.getTotalBranchScore());
                }

                voList.add(data);
            });
            return voList;
        }
        return null;

    }

    @Override
    public void updateById(EvaluationClinical evaluationClinical) {
        evaluationClinicalMapper.updateById(evaluationClinical);
    }

    @Override
    public void deleteByUserId(String userId) {
        evaluationClinicalMapper.deleteByUserId(userId);
    }

    @Transactional
    @Override
    public void updateStepByUserId(String userId) {
        //主表步骤
        EvaluationClinical evaluationClinical = evaluationClinicalMapper.list(new HashMap<String, String>() {{
            put("userId", userId);
            put("year", String.valueOf(LocalDate.now().getYear()));
        }}).get(0);
        int curMainStep = evaluationClinical.getStep();
        //主表回退步骤
        int finalMainStep = curMainStep - 1;
        //分数表步骤
        EvaluationClinicalScore score = scoreService.list(new HashMap<String, String>() {{
            put("userId", userId);
            put("year", String.valueOf(LocalDate.now().getYear()));
        }}).get(0);
        int curScoreStep = score.getStep();
        //分数表回退步骤
        int finalScoreStep = curScoreStep - 1;
        //更新主表
        evaluationClinicalMapper.updateStepByUserId(finalMainStep, userId);
        //更新分数表
        scoreService.updateStepByUserId(finalScoreStep, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchFinish(List<String> userIdList, int step) {
        evaluationClinicalMapper.batchFinish(userIdList, step);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void finishAll(int step) {
        evaluationClinicalMapper.finishAll(step);
    }

    @Override
    public void deleteWrongCliIds() {
        List<String> strings = evaluationClinicalMapper.selectWrongCliIds();
        for(String s: strings){
            evaluationClinicalMapper.deleteByUserId(s);
            scoreService.deleteByUserId(s);
        }
    }

    @Override
    public void deleteWrongNonCliIds() {
        List<String> strings =  nonClinicalMapper.selectWrongNonCliIds();
        for(String s: strings){
            nonClinicalMapper.deleteByUserId(s);
        }
    }
}
