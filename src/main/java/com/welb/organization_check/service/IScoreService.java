package com.welb.organization_check.service;


import com.welb.organization_check.entity.Score;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IScoreService
 * @projectName xh-360appraisal-interface
 * @description: 评价关系业务层接口
 * @date 2019/5/2710:48
 */
public interface  IScoreService {


    /**
     * 添加评分关系数据
     * @param score
     * @return
     */
    int insertSelective(Score score);

    /**
     * 修改评分关系数据
     * @param score
     * @return
     */
    int updateByPrimaryKeySelective(Score score);

    /**
     * 删除数据
     * @param id
     * @return
     */

    int deleteByPrimaryKey(String id);

    /**
     * 通过被评分人code查找被评分人关系数据
     * @param scoretype
     * @param scorredcode
     * @param username
     * @param stationcode
     * @return
     */
    List<Score> selectScoresByscorredAndUser(String scorredcode,String scoretype,String stationcode,String username);

    List<Score>selectScoresByScorredCode(String scorredcode,String scoretype);

    List<Score> selectScoresByscorredAndUserName(String scorredcode,String scoretype,String username);

    List<Score> selectScoresByscorredAndUserStationCode(String scorredcode,String scoretype,String stationcode);


    /**
     * 通过评分人code查找被评分人关系数据
     * @param scorredcode
     * @param scoretype
     * @return
     */
    List<Score> selectScoresByScorringCode(String scorredcode,String scoretype);

    List<Score> selectScoresByScorringAndUser(String scorredcode,String scoretype,String stationcode,String username);

    List<Score> selectScoresByScorringAndUserName(String scorredcode,String scoretype,String username);

    List<Score> selectScoresByScorringAndUserStationCode(String scorredcode,String scoretype,String stationcode);


    Score selectTypeByCode(String scorredcode,String scorringcode);

    /**
     * 通过被评人code查找所有数据
     * @param scorredcode
     * @return
     */
    List<Score>selectScoreByScorredCode(String scorredcode);

    /**
     * 通过评分人、被评分人code和评分类型查找score
     * @param score
     * @return
     */
    Score selectScoreByCodeAndType(Score score);


    /**
     * 通过主键查找score
     * @param id
     * @return
     */
    Score selectByPrimaryKey(String id);

    /**
     * 批量删除
     * @param scorredIds
     * @return
     */
    int batchDelete(List<String>scorredIds);

    /**
     * 通过被评人code查找所有数据的主键
     * @param scorredcode
     * @return
     */

    List<Score>selectIdByScorredCode(String scorredcode);

    /**
     * 通过评分人code查找所有数据
     * @param scorringcode
     * @return
     */
    List<Score>selectIdByScorringCode(String scorringcode);





    List<Score>findScoreAll();


}
