package com.welb.medicalEthics.service;

import com.welb.medicalEthics.dto.CalculateDto;

import java.util.Map;

/**
 * @description: 统计相关
 * @author: luox
 * @date： 2020/12/22
 */

public interface CalculateService {

    int cliCountByParams(Map<String,String> params);

    int nonCliCountByParams(Map<String,String> params);

    CalculateDto cliResultCalculate(Map<String, String> params);

    CalculateDto nonCliResultCalculate(Map<String,String> params);

    int selectPartyCount(String branchId);

}
