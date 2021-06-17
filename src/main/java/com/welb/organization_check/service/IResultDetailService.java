package com.welb.organization_check.service;

import com.welb.organization_check.entity.ResultDetail;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IResultDetailService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/2611:49
 */
public interface IResultDetailService {

    int deleteByPrimaryKey(Integer id);

    int insertSelective(ResultDetail detail);

    ResultDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ResultDetail detail);

    List<ResultDetail> selectResultDetailByReportCode(Integer code);

    int batchDelete(List<Integer>resultDetailIds);


}
