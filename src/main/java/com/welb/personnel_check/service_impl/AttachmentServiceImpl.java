package com.welb.personnel_check.service_impl;

import com.welb.personnel_check.entity.Attachment;
import com.welb.personnel_check.mapper.AttachmentMapper;
import com.welb.personnel_check.service.IAttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: AttachmentServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/139:57
 */
@Service
@Transactional
public class AttachmentServiceImpl implements IAttachmentService {
    @Resource
    AttachmentMapper attachmentMapper;
    @Override
    public int deleteByPrimaryKey(String attachmentcode) {
        return attachmentMapper.deleteByPrimaryKey(attachmentcode);
    }

    @Override
    public int insertSelective(Attachment attachment) {
        //主键自增长
        String attachmentCode= attachmentMapper.selectMaxCode();
        if (attachmentCode==null){
            attachment.setAttachmentcode("100");
        }else {
            int parseInt = Integer.parseInt(attachmentCode);
            parseInt++;
            String attachmentcode = String.valueOf(parseInt);
            attachment.setAttachmentcode(attachmentcode);
        }
        return attachmentMapper.insertSelective(attachment);
    }

    @Override
    public Attachment selectByPrimaryKey(String attachmentcode) {
        return attachmentMapper.selectByPrimaryKey(attachmentcode);
    }

    @Override
    public int updateByPrimaryKeySelective(Attachment attachment) {
        return attachmentMapper.updateByPrimaryKeySelective(attachment);
    }

    @Override
    public Attachment selectAttachmentByYearAndMonth(String year, String month,String moneycard) {
        return attachmentMapper.selectAttachmentByYearAndMonth(year, month,moneycard);
    }

    @Override
    public Attachment selectAttachmentByMoneyCard(String year, String month, String moneycard) {
        return attachmentMapper.selectAttachmentByMoneyCard(year, month, moneycard);
    }

    @Override
    public Attachment selectAttachmentByDepart(String year, String month, String depart) {
        return attachmentMapper.selectAttachmentByDepart(year, month, depart);
    }

    @Override
    public List<Attachment> getAttachmentList(Attachment attachment) {
        return attachmentMapper.getAttachmentList(attachment);
    }

}
