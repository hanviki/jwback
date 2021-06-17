package com.welb.organization_check.service_impl;


import com.welb.organization_check.entity.MessageLog;
import com.welb.organization_check.mapper.MessageLogMapper;
import com.welb.organization_check.service.IMessageLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: MessageLogServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/2215:43
 */
@Service
@Transactional
public class MessageLogServiceImpl implements IMessageLogService {
    @Resource
    MessageLogMapper logMapper;
    @Override
    public int deleteByPrimaryKey(String logcode) {
        return logMapper.deleteByPrimaryKey(logcode);
    }

    @Override
    public int insertSelective(MessageLog log) {
        String maxCode = logMapper.selectMaxCode();
        if (maxCode==null){
            log.setLogcode("100");
        }else {
            int i = Integer.parseInt(maxCode);
            i++;
            String logcode = String.valueOf(i);
            log.setLogcode(logcode);
        }
        return logMapper.insertSelective(log);
    }

    @Override
    public MessageLog selectByPrimaryKey(String logcode) {
        return logMapper.selectByPrimaryKey(logcode);
    }

    @Override
    public int updateByPrimaryKeySelective(MessageLog log) {
        return logMapper.updateByPrimaryKeySelective(log);
    }
}
