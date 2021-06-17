package com.welb.medicalEthics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: luox
 * @date： 2021/1/30
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CalculateDto {

    private int totalLevelOneCount;
    private int totalLevelTwoCount;
    private int totalLevelThreeCount;
    private int totalLevelFourCount;
    private int totalCount;
    private Double levelOnePercent;
    private Double levelTwoPercent;
    private Double levelThreePercent;
    private Double levelFourPercent;


}
