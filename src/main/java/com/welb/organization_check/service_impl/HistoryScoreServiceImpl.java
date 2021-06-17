package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.HistoryScore;
import com.welb.organization_check.mapper.HistoryScoreMapper;
import com.welb.organization_check.service.IHistoryScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: HistoryScoreServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2020/1/2010:17
 */
@Service
@Transactional
public class HistoryScoreServiceImpl implements IHistoryScoreService {
    @Resource
    HistoryScoreMapper historyScoreMapper;
    @Override
    public int batchInsert(List<HistoryScore> list) {
        return historyScoreMapper.batchInsert(list);
    }

    @Override
    public List<HistoryScore> selectAll(String year, String month) {
        return historyScoreMapper.selectAll(year, month);
    }

    @Override
    public int batchDelete(List<Integer> ids) {
        return historyScoreMapper.batchDelete(ids);
    }

    @Override
    public List<HistoryScore> selectList(HistoryScore score) {
        return historyScoreMapper.selectList(score);
    }

    @Override
    public int batchUpdate(List<HistoryScore> list) {
        return historyScoreMapper.batchUpdate(list);
    }


}
