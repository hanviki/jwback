package com.welb.medicalEthics.service;

import com.welb.medicalEthics.dto.EvaluationClinicalScoreDto;
import com.welb.medicalEthics.dto.ScoreDto;
import com.welb.medicalEthics.entity.EvaluationClinicalScore;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: luox
 * @date： 2020/11/19 0:25
 */

public interface EvaluationClinicalScoreService {

    /**
     * 插入单条数据
     *
     * @param evaluationClinicalScore
     */
    void insertScore(EvaluationClinicalScore evaluationClinicalScore);

    /**
     * 为用户生成空白分数
     *
     * @param userId
     * @return
     */
    Boolean generateFormItem(String userId);

    void update(EvaluationClinicalScore score);

    List<EvaluationClinicalScore> getFormData(Map<String, String> params);

    void batchUpdate(List<EvaluationClinicalScore> scores);

    void score(List<EvaluationClinicalScore> scores, String roleCode, int step, String userName);

    void reScore(EvaluationClinicalScoreDto dto, String userName);

    List<EvaluationClinicalScore> list(Map<String, String> params);

    ScoreDto calculate(String userId, String year);

    ScoreDto calculateScore(List<EvaluationClinicalScore> scoreList);

    void updateScoreInfo(String userId, String year);

    Boolean checkScore(List<EvaluationClinicalScore> scoreList);

    void deleteByUserId(String userId);

    void updateStepByUserId(int step, String userId);
}
