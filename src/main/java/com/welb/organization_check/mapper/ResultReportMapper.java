package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ResultReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ResultReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ResultReport record);

    ResultReport selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ResultReport record);

    List<ResultReport>selectResultReportByEvaluationCode(Integer evaluationCode);

    ResultReport selectResultReportByCode(@Param("resultreportcode")String resultreportcode,@Param("evaluationreportcode")Integer evaluationreportcode);

    int batchDelete(List<Integer>resultReportIds);
}
