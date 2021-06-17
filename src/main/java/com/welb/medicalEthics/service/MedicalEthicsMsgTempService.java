package com.welb.medicalEthics.service;

import com.welb.medicalEthics.entity.MedicalEthicsMsgTemp;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Map;

public interface MedicalEthicsMsgTempService {

    List<MedicalEthicsMsgTemp> list(Map<String,String> params);

    MedicalEthicsMsgTemp selectByUserId(String userId);

    void delete(int id);

    void update(MedicalEthicsMsgTemp medicalEthicsMsgTemp);

    void insert(MedicalEthicsMsgTemp medicalEthicsMsgTemp, ModelMap map);

    void insert(MedicalEthicsMsgTemp medicalEthicsMsgTemp);

    List<String> batchInsert(List<MedicalEthicsMsgTemp> dataSource);

    void batchUpdate(List<MedicalEthicsMsgTemp> dataSource);
}
