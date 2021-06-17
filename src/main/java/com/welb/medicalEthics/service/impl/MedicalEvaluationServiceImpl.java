package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.service.EvaluationClinicalService;
import com.welb.medicalEthics.service.EvaluationNonClinicalService;
import com.welb.medicalEthics.service.MedicalEthicsMsgService;
import com.welb.medicalEthics.service.MedicalEvaluationService;
import com.welb.sysBase.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/31
 */

@Service
public class MedicalEvaluationServiceImpl implements MedicalEvaluationService {

    @Autowired
    private EvaluationClinicalService clinicalService;

    @Autowired
    private EvaluationNonClinicalService nonClinicalService;

    @Override
    public void batchFinish(List<String> userIdList,int step) {
        try {
            clinicalService.batchFinish(userIdList, step);
            nonClinicalService.batchFinish(userIdList,step);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void finishAll(int step) {
        clinicalService.finishAll(step);
        nonClinicalService.finishAll(step);
    }
}
