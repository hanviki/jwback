package com.welb.organization_check.service;

import com.welb.organization_check.entity.EvaluateDelete;

import java.util.List;


/**
 * @author luoyaozu
 * @title: IEvaluateDeleteService
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/10/168:58
 */
public interface IEvaluateDeleteService {
    int deleteByPrimaryKey(Integer evaluateid);

    int insertSelective(EvaluateDelete evaluateDelete);

    int batchInsert(List<Integer> reportIds);

    List<EvaluateDelete>selectAllEvaluateDelete();

    int batchDelete(List<Integer>evaluateIds);



}
