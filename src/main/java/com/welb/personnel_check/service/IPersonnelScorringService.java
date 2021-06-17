package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.PersonnelScorring;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IPersonnelScorringService
 * @projectName xh-360appraisal-interface
 * @description: 人事评分业务层接口
 * @date 2019/8/1210:53
 */
public interface IPersonnelScorringService {
    int deleteByPrimaryKey(Integer usercode);

    int insertSelective(PersonnelScorring record);

    PersonnelScorring selectByPrimaryKey(String moneycard);

    int updateByPrimaryKeySelective(PersonnelScorring record);

    List<PersonnelScorring> selectAllLike(String year,String month,String departmentname,String score1,String score2);

    List<PersonnelScorring>selectAllLikeByManager(PersonnelScorring personnel);

    List<PersonnelScorring>selectListByMonthAndYear(PersonnelScorring personnelScorring);

    List<PersonnelScorring> selectListByMonthAndYearAndDepartmentName(String year, String month,String departmentname);

    int batchInsert(List<PersonnelScorring>list);

    int batchDelete(List<Integer>usercodes);

    PersonnelScorring selectOne(PersonnelScorring scorring);

    void deleteOne(PersonnelScorring scorring);

}
