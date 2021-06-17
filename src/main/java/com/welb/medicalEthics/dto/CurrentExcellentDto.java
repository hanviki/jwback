package com.welb.medicalEthics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 当前支部评优百分比
 * @author: luox
 * @date： 2020/12/22
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentExcellentDto {

    /**
     * 当前支部优秀人数
     */
    private int currentExcellentCount;

    /**
     * 当前支部总人数
     */
    private int totalCount;

    /**
     * 支部最大的优秀比
     */
    private int maxExcellentPercent;

}
