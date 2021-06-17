package com.welb.organization_check.service;

import com.welb.organization_check.entity.MessageLog;

/**
 * @author luoyaozu
 * @title: IMessageLogMapper
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/2215:42
 */
public interface IMessageLogService{
    int deleteByPrimaryKey(String logcode);

    int insertSelective(MessageLog log);

    MessageLog selectByPrimaryKey(String logcode);

    int updateByPrimaryKeySelective(MessageLog log);
}
