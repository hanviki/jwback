package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.PersonnelUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PersonnelUserMapper {
    int deleteByPrimaryKey(String moneycard);


    int insertSelective(PersonnelUser personnelUser);

    PersonnelUser selectByPrimaryKey(String moneycard);

    int updateByPrimaryKeySelective(PersonnelUser personnelUser);

    List<PersonnelUser>selectAllPersonnelByLike(PersonnelUser personnelUser);

    List<PersonnelUser>selectList();

    List<PersonnelUser>selectListByDeptName(String departmentname);

    int batchInsert(@Param("list")List<PersonnelUser>list);

    List<PersonnelUser>selectListByPersonner(PersonnelUser personnelUser);

    List<PersonnelUser>selectPersonnelByDepartmentName(String departmentname);

    int updateFlagByMoneyCard(@Param("flag")String flag,@Param("moneycard")String moneycard);

    int updateFlag(@Param("flag")String flag,@Param("moneycard")String moneycard);

    PersonnelUser selectById(Integer id);

    @Select("<script> select distinct MoneyCard from user </script>")
    List<String> allUserIds();

}
