package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.MessageTemplate;
import com.welb.organization_check.mapper.MessageTemplateMapper;
import com.welb.organization_check.service.ITemplateMessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: TemplateMessageServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 短信模板业务层接口的实现类
 * @date 2019/8/1411:27
 */
@Service
@Transactional
public class TemplateMessageServiceImpl implements ITemplateMessageService {
    @Resource
    MessageTemplateMapper templateMapper;

    @Override
    public int insertSelective(MessageTemplate template) {
        String maxCode = templateMapper.seletMaxCode();
        if (maxCode==null){
            template.setTemplatecode("100");
        }else {
            int parseInt = Integer.parseInt(maxCode);
            parseInt++;
            String maxcode=String.valueOf(parseInt);
            template.setTemplatecode(maxcode);
        }
        return templateMapper.insertSelective(template);
    }

    @Override
    public int updateByPrimaryKeySelective(MessageTemplate template) {
        return templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public int deleteByPrimaryKey(String templatecode) {
        return templateMapper.deleteByPrimaryKey(templatecode);
    }

    @Override
    public List<MessageTemplate> selectAllTemplate(MessageTemplate template) {
        return templateMapper.selectAllTemplate(template);
    }

    @Override
    public MessageTemplate selectTemplateByPrimaryKey(String templatecode) {
        return templateMapper.selectTemplateByPrimaryKey(templatecode);
    }

    @Override
    public List<MessageTemplate> selectTemplateList(String flag) {
        return templateMapper.selectTemplateList(flag);
    }
}
