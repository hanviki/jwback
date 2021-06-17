package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.HistoryScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface HistoryScoreMapper {
    int deleteByPrimaryKey(Integer id);

    int batchInsert(@Param("list")List<HistoryScore>list);

    int insertSelective(HistoryScore historyScore);

    HistoryScore selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HistoryScore historyScore);

    List<HistoryScore>selectAll(@Param("year")String year,@Param("month")String month);

    int batchDelete(List<Integer> ids);

    List<HistoryScore>selectList(HistoryScore score);

    int batchUpdate(@Param("list")List<HistoryScore>list);

}
