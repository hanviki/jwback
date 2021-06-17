package com.welb.medicalEthics.service;

import java.util.List;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/31
 */

public interface MedicalEvaluationService {

    void batchFinish(List<String>userIdList,int step);

    void finishAll(int step);

}
