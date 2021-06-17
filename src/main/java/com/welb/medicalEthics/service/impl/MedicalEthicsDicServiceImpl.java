package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.mapper.EvaluationEthicsDicMapper;
import com.welb.medicalEthics.service.MedicalEthicsDicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/23
 */

@Service
public class MedicalEthicsDicServiceImpl implements MedicalEthicsDicService {

    @Autowired
    private EvaluationEthicsDicMapper dicMapper;

    @Override
    public String selectByCodeAndType(String dicType, String dicCode) {
        return dicMapper.selectByCodeAndType(dicType,dicCode);
    }
}
