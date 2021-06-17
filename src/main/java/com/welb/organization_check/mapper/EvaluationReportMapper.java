package com.welb.organization_check.mapper;

import com.welb.organization_check.dto.UserEvaluationDto;
import com.welb.organization_check.entity.EvaluationReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EvaluationReportMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(EvaluationReport report);

    int updateByPrimaryKeySelective(EvaluationReport report);

    EvaluationReport selectByPrimaryKey(Integer id);

    EvaluationReport selectReportByUserCode(EvaluationReport report);

    Double selectReportTotalScore(@Param("year")String year,@Param("month")String month);

    int selectReportCount(@Param("year")String year,@Param("month")String month);

    int selectMaxAndMinReportCount(@Param("year")String year,@Param("month")String month);

    List<EvaluationReport>selectAllEvaluationReport();

    List<UserEvaluationDto>selectAllEvaluationReportLike(UserEvaluationDto evaluationDto);


    int selectEvacationReportCount();

    Double selectSumOfMaxScore(@Param("year")String year,@Param("month")String month);

    Double selectSumOfMinScore(@Param("year")String year,@Param("month")String month);

    int selectMaxId();

    List<EvaluationReport>selectEvaluationByUserCode(String usercode);

    int batchDelete(List<Integer>reportIds);

    int updateAvgScore(@Param("avgscore")double avgscore,@Param("year")String year,@Param("month")String month);

}
