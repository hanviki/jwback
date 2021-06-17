package com.welb.medicalEthics.service;

import com.welb.medicalEthics.dto.MedicalPermDto;
import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;

import java.util.List;
import java.util.Map;

/**
 * @description: 医德医风权限相关
 * @author: luox
 * @date： 2021/2/1
 */

public interface MedicalPermService {

    List<MedicalPermDto> listAdmin(Map<String, String> params);

    List<MedicalPermDto> list(Map<String, String> params);

    void deleteAdminRole(String userId);

    void updateUserRole(MedicalPermDto dto);

    void insertUserRole(MedicalPermDto dto);

    List<MedicalEthicsMsg> selectUserByKey(String key);

    List<MedicalEthicsUserRole> selectBaseByUserId(String userId);

}
