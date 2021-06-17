package com.welb.medicalEthics.dto;

import com.welb.medicalEthics.entity.EvaluationClinicalScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/24
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationClinicalScoreDto {

    private List<EvaluationClinicalScore> scoreList;

    private String roleType;

}
