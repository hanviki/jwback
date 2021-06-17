package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.PersonnelAuthorization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PersonnelAuthorizationMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(PersonnelAuthorization authorization);

    PersonnelAuthorization selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PersonnelAuthorization authorization);

    int updateFlag(@Param("flag")String flag,@Param("endtime")String endtime,@Param("angent")String angent);

    List<PersonnelAuthorization>selectListByStart(String starttime);

    List<PersonnelAuthorization>selectListByLater(String endtime);

    int batchUpdate(List<String>agents);

    PersonnelAuthorization selectOne(PersonnelAuthorization authorization);

    int closeFlag(Integer id);

    List<PersonnelAuthorization>getHistoryAuthorizationList(PersonnelAuthorization authorization);

    PersonnelAuthorization findByAgent(String agent);

}
