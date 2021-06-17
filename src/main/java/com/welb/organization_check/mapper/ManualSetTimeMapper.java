package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ManualSetTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ManualSetTimeMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManualSetTime setTime);

    ManualSetTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManualSetTime setTime);

    int updateTimeByYearAndMonth(ManualSetTime setTime);

    ManualSetTime selectManualByYearAndMonth(@Param("year")String year,@Param("month")String month);

}
