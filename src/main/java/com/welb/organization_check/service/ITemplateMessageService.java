package com.welb.organization_check.service;

import com.welb.organization_check.entity.MessageTemplate;

import java.util.List;

/**
 * @author luoyaozu
 * @title: ITemplateMessageService
 * @projectName xh-360appraisal-interface
 * @description: 短信模板业务层接口
 * @date 2019/8/1410:57
 */
public interface ITemplateMessageService {
    int insertSelective(MessageTemplate template);

    int updateByPrimaryKeySelective(MessageTemplate template);

    int deleteByPrimaryKey(String templatecode);

    List<MessageTemplate> selectAllTemplate(MessageTemplate template);

    MessageTemplate selectTemplateByPrimaryKey(String templatecode);

    List<MessageTemplate>selectTemplateList(String flag);

}
