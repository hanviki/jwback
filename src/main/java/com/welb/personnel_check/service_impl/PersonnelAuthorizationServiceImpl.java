package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.PersonnelAuthorization;
import com.welb.personnel_check.mapper.PersonnelAuthorizationMapper;
import com.welb.personnel_check.service.IPersonnelAuthorizationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: PersonnelAuthorizationServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 人事部各部门长授权代理人接口的实现类
 * @date 2020/3/215:36
 */
@Service
@Transactional
public class PersonnelAuthorizationServiceImpl implements IPersonnelAuthorizationService {
    @Resource
    PersonnelAuthorizationMapper authorizationMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return authorizationMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(PersonnelAuthorization authorization) {
        return authorizationMapper.insertSelective(authorization);
    }

    @Override
    public PersonnelAuthorization selectByPrimaryKey(Integer id) {
        return authorizationMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(PersonnelAuthorization authorization) {
        return authorizationMapper.updateByPrimaryKeySelective(authorization);
    }

    @Override
    public int updateFlag(String flag,String endtime,String angent) {
        return authorizationMapper.updateFlag(flag,endtime, angent);
    }

    @Override
    public List<PersonnelAuthorization> selectListByStart(String starttime) {
        return authorizationMapper.selectListByStart(starttime);
    }

    @Override
    public List<PersonnelAuthorization> selectListByLater(String endtime) {
        return authorizationMapper.selectListByLater(endtime);
    }

    @Override
    public int batchUpdate(List<String> agents) {
        return authorizationMapper.batchUpdate(agents);
    }

    @Override
    public PersonnelAuthorization selectOne(PersonnelAuthorization authorization) {
        return authorizationMapper.selectOne(authorization);
    }

    @Override
    public int closeFlag(Integer id) {
        return authorizationMapper.closeFlag(id);
    }

    @Override
    public List<PersonnelAuthorization> getHistoryAuthorizationList(PersonnelAuthorization authorization) {
        return authorizationMapper.getHistoryAuthorizationList(authorization);
    }

    @Override
    public PersonnelAuthorization findByAgent(String agent) {
        return authorizationMapper.findByAgent(agent);
    }
}
