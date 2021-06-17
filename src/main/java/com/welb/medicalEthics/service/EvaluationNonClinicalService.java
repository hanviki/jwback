package com.welb.medicalEthics.service;

import com.welb.medicalEthics.entity.EvaluationNonClinical;

import java.util.List;
import java.util.Map;

/**
 * @description: 非临床人员考评
 * @author: luox
 * @date： 2020/11/17 10:15
 */

public interface EvaluationNonClinicalService {

    Integer insert(EvaluationNonClinical evaluationNonClinical);

    void updateById(EvaluationNonClinical evaluationNonClinical);

    EvaluationNonClinical selectByUserId(String userId);

    EvaluationNonClinical initInfo(String userId);

    Integer updateForm(EvaluationNonClinical evaluationNonClinical, String userName);

    Integer editForm(EvaluationNonClinical evaluationNonClinical, String userName, String roleType);

    Integer getCountByParams(Map<String, String> params);

    List<EvaluationNonClinical> list(Map<String, String> prams);

    List<EvaluationNonClinical> extendPartyInfo(List<EvaluationNonClinical> list);

    void updateStepByUserId(String userId);

    void deleteByUserId(String userId);


    void batchFinish(List<String> userIdList,int step);

    void finishAll(int step);
}
