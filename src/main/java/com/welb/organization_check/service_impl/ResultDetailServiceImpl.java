package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.ResultDetail;
import com.welb.organization_check.mapper.ResultDetailMapper;
import com.welb.organization_check.service.IResultDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ResultDetailServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/2611:50
 */
@Service
@Transactional
public class ResultDetailServiceImpl implements IResultDetailService {
    @Resource
    ResultDetailMapper detailMapper;
    @Override
    public int deleteByPrimaryKey(Integer id) {
        return detailMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertSelective(ResultDetail detail) {
        return detailMapper.insertSelective(detail);
    }

    @Override
    public ResultDetail selectByPrimaryKey(Integer id) {
        return detailMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ResultDetail detail) {
        return detailMapper.updateByPrimaryKeySelective(detail);
    }

    @Override
    public List<ResultDetail> selectResultDetailByReportCode(Integer code) {
        return detailMapper.selectResultDetailByReportCode(code);
    }

    @Override
    public int batchDelete(List<Integer> resultDetailIds) {
        return detailMapper.batchDelete(resultDetailIds);
    }
}
