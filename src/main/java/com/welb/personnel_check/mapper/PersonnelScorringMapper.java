package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.PersonnelScorring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonnelScorringMapper {
    int deleteByPrimaryKey(Integer usercode);

    int insertSelective(PersonnelScorring record);

    PersonnelScorring selectByPrimaryKey(String moneycard);

    int updateByPrimaryKeySelective(PersonnelScorring record);

    List<PersonnelScorring>selectAllLike(@Param("year")String year,@Param("month")String month,@Param("departmentname")String departmentname,
                                         @Param("score1")String score,@Param("score2")String score2);

    List<PersonnelScorring>selectAllLikeByManager(PersonnelScorring personnel);

    List<PersonnelScorring>selectListByMonthAndYear(PersonnelScorring personnelScorring);

    List<PersonnelScorring>selectListByMonthAndYearAndDepartmentName(@Param("year")String year, @Param("month")String month, @Param("departmentname")String departmentname);

    int batchInsert(@Param("list") List<PersonnelScorring>list);

    int batchDelete(List<Integer>usercodes);

    PersonnelScorring selectOne(PersonnelScorring scorring);

    void deleteOne(PersonnelScorring scorring);

}
