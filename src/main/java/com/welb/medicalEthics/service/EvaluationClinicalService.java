package com.welb.medicalEthics.service;

import com.welb.medicalEthics.entity.EvaluationClinical;

import java.util.List;
import java.util.Map;

/**
 * @description: 临床人员考评
 * @author: luox
 * @date： 2020/11/17 10:15
 */

public interface EvaluationClinicalService {

    Integer insert(EvaluationClinical evaluationClinical);

    EvaluationClinical initInfo(String userId);

    EvaluationClinical selectByUserId(String userId);

    Boolean updateBaseInfo(EvaluationClinical evaluationClinical);

    void updateSelfSummary(String userId, String selfSummary);

    void updateSelfSummaryTemp(String userId, String selfSummary);

    void updateByUserId(EvaluationClinical evaluationClinical);

    List<EvaluationClinical> list(Map<String,String> params);

    List<EvaluationClinical> extendPartyInfo(List<EvaluationClinical> list);

    void updateById(EvaluationClinical evaluationClinical);

    void deleteByUserId(String userId);

    void updateStepByUserId(String userId);

    void batchFinish(List<String> userIdList,int step);

    void finishAll(int step);

    void deleteWrongCliIds();

    public void deleteWrongNonCliIds();
}
