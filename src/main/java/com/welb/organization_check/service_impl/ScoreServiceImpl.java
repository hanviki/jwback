package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Score;
import com.welb.organization_check.mapper.ScoreMapper;
import com.welb.organization_check.service.IScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author luoyaozu
 * @title: ScoreServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 评价关系业务层接口的实现类
 * @date 2019/5/2710:54
 */
@Service
@Transactional
public class ScoreServiceImpl implements  IScoreService {
    @Resource
    ScoreMapper scoreMapper;

    @Override
    public int deleteByPrimaryKey(String id) {
        return scoreMapper.deleteByPrimaryKey(id);
    }
    @Override
    public int insertSelective(Score score) {
        String maxId = scoreMapper.selectMaxId();
        if (maxId==null){
            score.setId("100");
        }else {
            int num = Integer.parseInt(maxId.trim());
            num++;
            String id = String.valueOf(num);
            score.setId(id);
        }
        return scoreMapper.insertSelective(score);
    }

    @Override
    public int updateByPrimaryKeySelective(Score score) {
        return scoreMapper.updateByPrimaryKeySelective(score);
    }




    @Override
    public List<Score> selectScoresByscorredAndUser(String scorredcode,String scoretype,
                                                 String stationcode,String username) {
        return scoreMapper.selectScoresByscorredAndUser(scorredcode,scoretype,stationcode,username);
    }
    @Override
    public List<Score> selectScoresByscorredAndUserName(String scorredcode,String scoretype, String username) {
        return scoreMapper.selectScoresByscorredAndUserName(scorredcode,scoretype,username);
    }
    @Override
    public List<Score> selectScoresByScorredCode(String scorredcode,String scoretype) {
        return scoreMapper.selectScoresByScorredCode(scorredcode, scoretype);
    }
    @Override
    public List<Score> selectScoresByscorredAndUserStationCode(String scorredcode,String scoretype,
                                                    String stationcode) {
        return scoreMapper.selectScoresByscorredAndUserStationCode(scorredcode,scoretype,stationcode);
    }


    @Override
    public List<Score> selectScoresByScorringCode(String scorredcode,String scoretype) {
        return scoreMapper.selectScoresByScorringCode(scorredcode,scoretype);
    }
    @Override
    public List<Score> selectScoresByScorringAndUser(String scorredcode,String scoretype,
                                                  String stationcode,String username) {
        return scoreMapper.selectScoresByScorringAndUser(scorredcode,scoretype,stationcode, username);
    }
    @Override
    public List<Score> selectScoresByScorringAndUserName(String scorredcode,String scoretype,String username) {
        return scoreMapper.selectScoresByScorringAndUserName(scorredcode,scoretype, username);
    }

    @Override
    public List<Score> selectScoresByScorringAndUserStationCode(String scorredcode,String scoretype,String stationcode) {
        return scoreMapper.selectScoresByScorringAndUserStationCode(scorredcode,scoretype,stationcode);
    }

    @Override
    public Score selectTypeByCode(String scorredcode, String scorringcode) {
        return scoreMapper.selectTypeByCode(scorredcode, scorringcode);
    }

    @Override
    public List<Score> selectScoreByScorredCode(String scorredcode) {
        return scoreMapper.selectScoreByScorredCode(scorredcode);
    }

    @Override
    public Score selectScoreByCodeAndType(Score score) {
        return scoreMapper.selectScoreByCodeAndType(score);
    }

    @Override
    public Score selectByPrimaryKey(String id) {
        return scoreMapper.selectByPrimaryKey(id);
    }

    @Override
    public int batchDelete(List<String> scorredIds) {
        return scoreMapper.batchDelete(scorredIds);
    }

    @Override
    public List<Score> selectIdByScorredCode(String scorredcode) {
        return scoreMapper.selectIdByScorredCode(scorredcode);
    }

    @Override
    public List<Score> selectIdByScorringCode(String scorringcode) {
        return scoreMapper.selectIdByScorringCode(scorringcode);
    }

    @Override
    public List<Score> findScoreAll() {
        return scoreMapper.findScoreAll();
    }

}
