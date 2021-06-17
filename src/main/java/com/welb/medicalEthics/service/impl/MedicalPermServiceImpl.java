package com.welb.medicalEthics.service.impl;

import com.welb.medicalEthics.dto.MedicalPermDto;
import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;
import com.welb.medicalEthics.mapper.MedicalPermMapper;
import com.welb.medicalEthics.service.MedicalPermService;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @description: 医德权限管理相关
 * @author: luox
 * @date： 2021/2/1
 */

@Service
public class MedicalPermServiceImpl implements MedicalPermService {

    @Autowired
    private MedicalPermMapper medicalPermMapper;


    @Override
    public List<MedicalPermDto> listAdmin(Map<String, String> params) {
        return medicalPermMapper.listAdmin(params);
    }

    @Override
    public List<MedicalPermDto> list(Map<String, String> params) {
        return medicalPermMapper.list(params);
    }

    @Override
    public void deleteAdminRole(String userId) {
        medicalPermMapper.deleteAdminRole(userId);
    }

    /**
     * 更新用户角色
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserRole(MedicalPermDto dto) {
        String userId = dto.getUserId();
        deleteAdminRole(userId);
        MedicalPermDto newData = new MedicalPermDto();
        newData.setRoleId(dto.getRoleId());
        newData.setUserId(userId);
        newData.setStatus(dto.getStatus());
        medicalPermMapper.updateStatus(dto.getStatus(),userId);
        medicalPermMapper.insertUserRole(newData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserRole(MedicalPermDto dto){
        String userId = dto.getUserId();
        String roleId = dto.getRoleId();
        String isDelete = dto.getStatus();
        MedicalPermDto newData = new MedicalPermDto();
        newData.setUserId(userId);
        newData.setRoleId(roleId);
        newData.setStatus(isDelete);
        medicalPermMapper.insertUserRole(newData);
        //更新user状态
        medicalPermMapper.updateStatus(dto.getStatus(),userId);
    }

    @Override
    public List<MedicalEthicsMsg> selectUserByKey(String key) {
        return medicalPermMapper.selectUserByKey(key);
    }

    @Override
    public List<MedicalEthicsUserRole> selectBaseByUserId(String userId) {
        return medicalPermMapper.selectBaseByUserId(userId);
    }

}
