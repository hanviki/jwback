package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ScoreHistory;
import com.welb.organization_check.mapper.ScoreHistoryMapper;
import com.welb.organization_check.service.IScoreHistoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ScoreHistoryServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 历史评分数据接口的实现类
 * @date 2020/7/21
 */
@Service
@Transactional
public class ScoreHistoryServiceImpl implements IScoreHistoryService {
    @Resource
    ScoreHistoryMapper historyMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return historyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ScoreHistory history) {
        return historyMapper.insertSelective(history);
    }

    @Override
    public ScoreHistory selectByPrimaryKey(Integer id) {
        return historyMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ScoreHistory history) {
        return historyMapper.updateByPrimaryKeySelective(history);
    }

    @Override
    public ScoreHistory selectOneByHistory(ScoreHistory history) {
        return historyMapper.selectOneByHistory(history);
    }

    @Override
    public List<ScoreHistory> selectHistoryList(ScoreHistory history) {
        return historyMapper.selectHistoryList(history);
    }

    @Override
    public List<ScoreHistory> gradeList(ScoreHistory history) {
        return historyMapper.gradeList(history);
    }

    @Override
    public List<ScoreHistory> selectGradeHisotyList(ScoreHistory history) {
        return historyMapper.selectGradeHisotyList(history);
    }

    @Override
    public List<ScoreHistory> selectUserHisotyList(ScoreHistory history) {
        return historyMapper.selectUserHisotyList(history);
    }

    @Override
    public int batchInsert(List<ScoreHistory> list) {
        return historyMapper.batchInsert(list);
    }

    @Override
    public ScoreHistory selectScoreHistoryByUserCode(String usercode) {
        return historyMapper.selectScoreHistoryByUserCode(usercode);
    }

    @Override
    public List<ScoreHistory> oneClickDown(String year, String month) {
        return historyMapper.oneClickDown(year, month);
    }
    @Override
    public List<ScoreHistory> findScoreHistoryList(String year, String month) {
        return historyMapper.findScoreHistoryList(year, month);
    }

    @Override
    public List<ScoreHistory> findUserScoreHistory(String year, String month) {
        return historyMapper.findUserScoreHistory(year, month);
    }
}
