package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.HrpUser;
import com.welb.organization_check.mapper.HrpUserMapper;
import com.welb.organization_check.service.IHrpUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: HrpUserServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/2910:51
 */
@Service
@Transactional
public class HrpUserServiceImpl implements IHrpUserService {
    @Resource
    HrpUserMapper hrpUserMapper;

    @Override
    public HrpUser selectByPrimaryKey(String uId) {
        return hrpUserMapper.selectByPrimaryKey(uId);
    }

    @Override
    public List<HrpUser> selectAll() {
        return hrpUserMapper.selectAll();
    }
}
