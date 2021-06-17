package com.welb.organization_check.service;

import com.welb.organization_check.entity.ManualSetTime;

/**
 * @author luoyaozu
 * @title: IManualSetTimeService
 * @projectName xh-360appraisal-interface
 * @description: 手动设置考核时间接口
 * @date 2020/2/1611:55
 */
public interface IManualSetTimeService {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ManualSetTime setTime);

    ManualSetTime selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ManualSetTime setTime);

    int updateTimeByYearAndMonth(ManualSetTime setTime);

    ManualSetTime selectManualByYearAndMonth(String year,String month);
}
