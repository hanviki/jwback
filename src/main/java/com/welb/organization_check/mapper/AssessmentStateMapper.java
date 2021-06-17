package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.AssessmentState;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AssessmentStateMapper {
    int deleteByPrimaryKey(Integer id);

    int insertSelective(AssessmentState state);

    AssessmentState selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AssessmentState state);

}
