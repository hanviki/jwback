package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.EvaluateDelete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface EvaluateDeleteMapper {
    int deleteByPrimaryKey(Integer evaluateid);

    int insertSelective(EvaluateDelete evaluateDelete);

    int batchInsert(List<Integer> reportIds);

    List<EvaluateDelete>selectAllEvaluateDelete();

    int batchDelete(List<Integer>evaluateIds);
}
