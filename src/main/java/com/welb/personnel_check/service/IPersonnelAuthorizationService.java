package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.PersonnelAuthorization;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IPersonnelAuthorizationService
 * @projectName xh-360appraisal-interface
 * @description: 人事部各部门长授权代理人接口
 * @date 2020/3/215:32
 */
public interface IPersonnelAuthorizationService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(PersonnelAuthorization authorization);

    PersonnelAuthorization selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonnelAuthorization authorization);

    int updateFlag(String flag,String endtime,String angent);

    List<PersonnelAuthorization> selectListByStart(String starttime);

    List<PersonnelAuthorization>selectListByLater(String endtime);

    int batchUpdate(List<String>agents);

    PersonnelAuthorization selectOne(PersonnelAuthorization authorization);

    int closeFlag(Integer id);

    List<PersonnelAuthorization>getHistoryAuthorizationList(PersonnelAuthorization authorization);

    PersonnelAuthorization findByAgent(String agent);

}
