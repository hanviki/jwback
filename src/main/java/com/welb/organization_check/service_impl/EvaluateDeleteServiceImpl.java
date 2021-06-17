package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.EvaluateDelete;
import com.welb.organization_check.mapper.EvaluateDeleteMapper;
import com.welb.organization_check.service.IEvaluateDeleteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: EvaluateDeleteServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/10/169:00
 */
@Service
@Transactional
public class EvaluateDeleteServiceImpl implements IEvaluateDeleteService {
    @Resource
    EvaluateDeleteMapper evaluateDeleteMapper;
    @Override
    public int deleteByPrimaryKey(Integer evaluateid) {
        return evaluateDeleteMapper.deleteByPrimaryKey(evaluateid);
    }

    @Override
    public int insertSelective(EvaluateDelete evaluateDelete) {
        return evaluateDeleteMapper.insertSelective(evaluateDelete);
    }

    @Override
    public int batchInsert(List<Integer> reportIds) {
        return evaluateDeleteMapper.batchInsert(reportIds);
    }

    @Override
    public List<EvaluateDelete> selectAllEvaluateDelete() {
        return evaluateDeleteMapper.selectAllEvaluateDelete();
    }

    @Override
    public int batchDelete(List<Integer> evaluateIds) {
        return evaluateDeleteMapper.batchDelete(evaluateIds);
    }
}
