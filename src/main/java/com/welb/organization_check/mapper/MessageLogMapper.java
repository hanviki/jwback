package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.MessageLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageLogMapper {
    int deleteByPrimaryKey(String logcode);

    int insertSelective(MessageLog log);

    MessageLog selectByPrimaryKey(String logcode);

    int updateByPrimaryKeySelective(MessageLog log);

    String selectMaxCode();

}
