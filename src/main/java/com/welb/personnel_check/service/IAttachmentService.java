package com.welb.personnel_check.service;

import com.welb.personnel_check.entity.Attachment;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IAttachmentService
 * @projectName xh-360appraisal-interface
 * @description: 人事部附件上传业务层接口
 * @date 2019/8/139:55
 */
public interface IAttachmentService {
    int deleteByPrimaryKey(String attachmentcode);

    int insertSelective(Attachment attachment);

    Attachment selectByPrimaryKey(String attachmentcode);

    int updateByPrimaryKeySelective(Attachment attachment);

    Attachment selectAttachmentByYearAndMonth(String year, String month,String moneycard);

    Attachment selectAttachmentByMoneyCard(String year,String month,String moneycard);

    Attachment selectAttachmentByDepart(String year,String month,String depart);

    List<Attachment> getAttachmentList(Attachment attachment);

}
