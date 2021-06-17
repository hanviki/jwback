package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ResultDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResultDetailMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(ResultDetail detail);

    ResultDetail selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ResultDetail detail);

    List<ResultDetail>selectResultDetailByReportCode(Integer code);

    int batchDelete(List<Integer>resultDetailIds);
}
