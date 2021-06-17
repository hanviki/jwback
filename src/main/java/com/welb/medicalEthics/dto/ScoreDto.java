package com.welb.medicalEthics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: luox
 * @date： 2020/11/27
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreDto {

    /**
     * 总得分
     */
    private String score;

    /**
     * 评分等级
     */
    private int level;

    /**
     * 总评分
     */
    private Integer totalSelfScore;

    /**
     * 总科室评分
     */
    private Integer totalHeadScore;

    /**
     * 总支部评分
     */
    private Integer totalBranchScore;

}
