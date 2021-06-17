package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ResultReport;
import com.welb.organization_check.mapper.ResultReportMapper;
import com.welb.organization_check.service.IResultReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ResultReportServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 综合结果报告业务层接口的实现类
 * @date 2019/6/200:03
 */
@Service
@Transactional
public class ResultReportServiceImpl implements IResultReportService {
    @Resource
    ResultReportMapper reportMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return reportMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ResultReport record) {
        return reportMapper.insertSelective(record);
    }

    @Override
    public ResultReport selectByPrimaryKey(Integer id) {
        return reportMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ResultReport record) {
        return reportMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<ResultReport> selectResultReportByEvaluationCode(Integer evaluationCode) {
        return reportMapper.selectResultReportByEvaluationCode(evaluationCode);
    }

    @Override
    public ResultReport selectResultReportByCode(String resultreportcode, Integer evaluationreportcode) {
        return reportMapper.selectResultReportByCode(resultreportcode, evaluationreportcode);
    }



    @Override
    public int batchDelete(List<Integer> resultReportIds) {
        return reportMapper.batchDelete(resultReportIds);
    }
}
