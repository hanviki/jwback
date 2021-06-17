package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.MessageTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageTemplateMapper {

    int insertSelective(MessageTemplate template);

    int updateByPrimaryKeySelective(MessageTemplate template);

    int deleteByPrimaryKey(String messageTemplateCode);

    String seletMaxCode();

    List<MessageTemplate>selectAllTemplate(MessageTemplate template);

    List<MessageTemplate>selectTemplateList(String flag);

    MessageTemplate selectTemplateByPrimaryKey(String messageTemplateCode);



}
