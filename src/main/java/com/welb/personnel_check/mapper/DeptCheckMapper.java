package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.DeptCheck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeptCheckMapper {

    int insertSelective(DeptCheck deptCheck);

    int updateByPrimaryKeySelective(DeptCheck deptCheck);

    DeptCheck selectDeptCheckByYear(@Param("year")String year,@Param("moneyCard")String moneyCard);

    DeptCheck selectDeptCheckByYearAndMoneyCard(@Param("year") String year,@Param("moneycard")String moneycard);

    DeptCheck selectDeptCheckByYearAndDepart(@Param("year") String year,@Param("depart")String depart);

    String selectMaxCode();

    List<DeptCheck>selectList(DeptCheck deptCheck);


}
