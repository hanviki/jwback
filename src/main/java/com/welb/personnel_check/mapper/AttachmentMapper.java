package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.Attachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AttachmentMapper {
    int deleteByPrimaryKey(String attachmentcode);

    int insertSelective(Attachment attachment);

    Attachment selectByPrimaryKey(String attachmentcode);

    int updateByPrimaryKeySelective(Attachment attachment);

    Attachment selectAttachmentByYearAndMonth(@Param("year")String year,@Param("month")String month,@Param("moneycard")String moneycard);

    Attachment selectAttachmentByMoneyCard(@Param("year")String year,@Param("month")String month,@Param("moneycard")String moneycard);

    String selectMaxCode();

    Attachment selectAttachmentByDepart(@Param("year")String year,@Param("month")String month,@Param("depart")String depart);

    List<Attachment>getAttachmentList(Attachment attachment);

}
