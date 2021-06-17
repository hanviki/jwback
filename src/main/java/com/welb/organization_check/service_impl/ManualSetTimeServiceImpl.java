package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.mapper.ManualSetTimeMapper;
import com.welb.organization_check.service.IManualSetTimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: ManualSetTimeServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 手动设置考核时间接口的实现类
 * @date 2020/2/1611:56
 */
@Service
@Transactional
public class ManualSetTimeServiceImpl implements IManualSetTimeService {
    @Resource
    ManualSetTimeMapper setTimeMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return setTimeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ManualSetTime setTime) {
        return setTimeMapper.insertSelective(setTime);
    }

    @Override
    public ManualSetTime selectByPrimaryKey(Integer id) {
        return setTimeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ManualSetTime setTime) {
        return setTimeMapper.updateByPrimaryKeySelective(setTime);
    }

    @Override
    public int updateTimeByYearAndMonth(ManualSetTime setTime) {
        return setTimeMapper.updateTimeByYearAndMonth(setTime);
    }

    @Override
    public ManualSetTime selectManualByYearAndMonth(String year, String month) {
        return setTimeMapper.selectManualByYearAndMonth(year, month);
    }
}
