package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Score;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreMapper {
    int deleteByPrimaryKey(String id);

    int insert(Score record);

    int insertSelective(Score record);

    Score selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Score record);

    int updateByPrimaryKey(Score record);

    String selectMaxId();

    List<Score>selectScoresByscorredAndUser(@Param("scorredcode")String scorredcode,
                                         @Param("scoretype")String scoretype,
                                         @Param("stationcode")String stationcode,
                                         @Param("username") String username);

    List<Score>selectScoresByScorredCode(@Param("scorredcode")String scorredcode,
                                         @Param("scoretype")String scoretype);

    List<Score>selectScoresByscorredAndUserName(@Param("scorredcode")String scorredcode,
                                                @Param("scoretype")String scoretype,
                                                @Param("username") String username);

    List<Score>selectScoresByscorredAndUserStationCode(@Param("scorredcode")String scorredcode,
                                                       @Param("scoretype")String scoretype,
                                                       @Param("stationcode")String stationcode);


    List<Score>selectScoresByScorringCode(@Param("scorredcode")String scorredcode,@Param("scoretype")String scoretype);

    List<Score>selectScoresByScorringAndUser(@Param("scorredcode")String scorredcode,
                                          @Param("scoretype")String scoretype,
                                          @Param("stationcode")String stationcode,
                                          @Param("username") String username);
    List<Score>selectScoresByScorringAndUserName(@Param("scorredcode")String scorredcode,
                                          @Param("scoretype")String scoretype,
                                          @Param("username") String username);

    List<Score>selectScoresByScorringAndUserStationCode(@Param("scorredcode")String scorredcode,
                                             @Param("scoretype")String scoretype,
                                             @Param("stationcode")String stationcode);

    Score selectTypeByCode(@Param("scorredcode")String scorredcode,@Param("scorringcode")String scorringcode);

    List<Score>selectScoreByScorredCode(String scorredcode);

    List<Score>selectIdByScorredCode(String scorredcode);

    List<Score>selectIdByScorringCode(String scorringcode);

    Score selectScoreByCodeAndType(Score score);

    int batchDelete(List<String>scorredIds);


    List<Score>findScoreAll();




}
