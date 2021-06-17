package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.dto.CalculateDto;
import com.welb.medicalEthics.mapper.CalculateMapper;
import com.welb.medicalEthics.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @description: 统计相关
 * @author: luox
 * @date： 2020/12/22
 */

@Service
public class CalculateServiceImpl implements CalculateService {

    @Autowired
    private CalculateMapper calculateMapper;

    @Override
    public int cliCountByParams(Map<String, String> params) {
        return calculateMapper.cliCountByParams(params);
    }

    /**
     * 非临床的数量统计
     * @param params
     * @return
     */
    @Override
    public int nonCliCountByParams(Map<String, String> params) {
        return calculateMapper.nonCliCountByParams(params);
    }

    @Override
    public CalculateDto cliResultCalculate(Map<String, String> params) {
        return calculateMapper.cliResultCalculate(params);
    }

    @Override
    public CalculateDto nonCliResultCalculate(Map<String, String> params) {
        return calculateMapper.nonCliResultCalculate(params);
    }

    @Override
    public int selectPartyCount(String branchId) {
        return calculateMapper.selectPartyCount(branchId);
    }
}
