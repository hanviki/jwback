package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.ExcelLog;
import com.welb.personnel_check.mapper.ExcelLogMapper;
import com.welb.personnel_check.service.IExcelLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: ExcelLogServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 上传分数excel的行为日志业务层接口的实现类
 * @date 2020/4/2610:11
 */
@Service
@Transactional
public class ExcelLogServiceImpl implements IExcelLogService {
    @Resource
    ExcelLogMapper logMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return logMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ExcelLog log) {
        return logMapper.insertSelective(log);
    }

    @Override
    public ExcelLog selectByPrimaryKey(Integer id) {
        return logMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ExcelLog log) {
        return logMapper.updateByPrimaryKeySelective(log);
    }
}
