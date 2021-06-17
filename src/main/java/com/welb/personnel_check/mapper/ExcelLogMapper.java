package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.ExcelLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExcelLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ExcelLog log);

    ExcelLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExcelLog log);

}
