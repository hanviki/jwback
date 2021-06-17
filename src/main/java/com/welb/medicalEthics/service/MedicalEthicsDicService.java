package com.welb.medicalEthics.service;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/23
 */

public interface MedicalEthicsDicService {

    String selectByCodeAndType(String dicType,String dicCode);

}
