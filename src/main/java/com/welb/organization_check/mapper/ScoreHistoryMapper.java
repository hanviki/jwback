package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreHistoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ScoreHistory history);

    ScoreHistory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ScoreHistory history);

    ScoreHistory selectOneByHistory(ScoreHistory history);

    List<ScoreHistory>selectHistoryList(ScoreHistory history);

    List<ScoreHistory>gradeList(ScoreHistory history);

    List<ScoreHistory>selectGradeHisotyList(ScoreHistory history);

    List<ScoreHistory>selectUserHisotyList(ScoreHistory history);

    int batchInsert(List<ScoreHistory>list);

    ScoreHistory selectScoreHistoryByUserCode(String usercode);

    List<ScoreHistory>oneClickDown(@Param("year")String year,@Param("month")String month);

    List<ScoreHistory>findScoreHistoryList(@Param("year")String year,@Param("month")String month);

}
