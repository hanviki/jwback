package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.MonthSummary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MonthSummaryMapper {
    int deleteByPrimaryKey(String serialno);

    int insert(MonthSummary summary);

    int insertSelective(MonthSummary summary);

    int updateSubmitStateBySerialNo(String serialno);

    int updateGradeStateBySerialNo(String serialno);

    int updateStateAll();

    int updateFinishGradeBySerialNo(String serialno);

    int updateFinishGradeAll();

    MonthSummary selectByPrimaryKey(String serialno);

    int updateByPrimaryKeySelective(MonthSummary summary);

    int updateByPrimaryKey(MonthSummary summary);

    List<MonthSummary>selectSummaryLikeAll(MonthSummary summary);

    int updateStateBySerialNo(MonthSummary summary);

    List<MonthSummary>selectSummaryByYearAndMonth(@Param("year")String year,@Param("month")String month);

    List<MonthSummary>selectSummaryListByYearAndMonth(@Param("year")String year,@Param("month")String month);

    MonthSummary selectSummaryByYearAndMonthAndCode(@Param("year")String year,@Param("month")String month,@Param("employeecode")String employeecode);

    List<MonthSummary>findMonthSummaryAll();

    int batchDelete(List<String>serialnos);

    List<MonthSummary>selectSerialNoByEmployeeCode(String employeecode);

    int batchInsert(@Param("list") List<MonthSummary>list);

    int batchUpdate(@Param("list") List<MonthSummary>list);

    List<MonthSummary>selectListByYearAndMonth(@Param("year")String year,@Param("month")String month);

}
