package com.welb.medicalEthics.vo;

import com.welb.medicalEthics.entity.EvaluationClinicalScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 问卷表单
 * @author: luox
 * @date： 2020/11/25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationClinicalScoreVo {

    /**
     * 分类title
     */
    private String title;

    /**
     * 题目列表
     */
    private List<EvaluationClinicalScore> content;

}
