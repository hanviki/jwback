package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreFlowMapper {
    int deleteByPrimaryKey(String serialno);

    int insert(ScoreFlow record);

    int insertSelective(ScoreFlow record);

    ScoreFlow selectByPrimaryKey(String serialno);

    int updateByPrimaryKeySelective(ScoreFlow record);

    int updateByPrimaryKey(ScoreFlow record);

    List<ScoreFlow> selectByScoreFlow(@Param("mserialno") String mserialno,@Param("scorringcode")String scorringcode);

    ScoreFlow getTotal(ScoreFlow scoreflow);

    Double getScoreByType(@Param("mserialno")String mserialno,@Param("scoretype")String scoretype);

    int getScoreByTypeCount(@Param("mserialno")String mserialno,@Param("scoretype")String scoretype);

    String selectMaxSerialNo();

    List<ScoreFlow>selectScoreAllByScoredCode(@Param("scoredcode")String scoredcode,@Param("year")String year,@Param("month")String month);

    Double selectMaxScoreByScoredCode(@Param("scoredcode")String scoredcode,@Param("year")String year,@Param("month")String month);

    Double selectMinScoreByScoredCode(@Param("scoredcode")String scoredcode,@Param("year")String year,@Param("month")String month);

    List<ScoreFlow> selectFlowByScorredCode(String scorredcode);

    List<ScoreFlow> selectFlowByScorringCode(String scorringcode);

    List<ScoreFlow> getSingleTotalScoreAll(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype,
                       @Param("dutytype") String dutytype,@Param("orderid")Integer orderid);

    List<ScoreFlow> selectScoreByCodeAndType(ScoreFlow scoreFlow);

    int batchDelete(List<String>scorringSerialnos);

    int batchUpdate(@Param("list") List<ScoreFlow>list);

    int batchInsert(@Param("list") List<ScoreFlow>list);

    ScoreFlow selectFlowByMserialNoAndScorringCode(@Param("mserialno") String mserialno,@Param("scorringcode")String scorringcode);

    Double getTypeAvgScore(@Param("mserialno")String mserialno,@Param("scoretype")String scoretype);

    int getTotalCount(@Param("mserialno") String mserialno,@Param("scorringcode")String scorringcode);

    List<ScoreFlow>findFlowsByCode(String usercode);

}
