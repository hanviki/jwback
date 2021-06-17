package com.welb.organization_check.service;

import com.welb.organization_check.entity.HistoryScore;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IHistoryScoreService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2020/1/2010:16
 */
public interface IHistoryScoreService {

    int batchInsert(List<HistoryScore> list);

    List<HistoryScore>selectAll(String year,String month);

    int batchDelete(List<Integer> ids);

    List<HistoryScore>selectList(HistoryScore score);

    int batchUpdate(List<HistoryScore>list);

}
