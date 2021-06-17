// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3)
// Source File Name:   ScoreDetailMapper.java

package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.ScoreDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScoreDetailMapper {

    int deleteByPrimaryKey(String s);

    int insert(ScoreDetail scoredetail);

    int insertSelective(ScoreDetail scoredetail);

    ScoreDetail selectByPrimaryKey(String s);

    int updateByPrimaryKeySelective(ScoreDetail scoredetail);

    int updateByPrimaryKey(ScoreDetail scoredetail);

    ScoreDetail selectDetailBySerialNo(@Param("dutycode") String dutycode, @Param("fserialno") String fserialno);


    Double getTotalScoreByType(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype, @Param("dutytype") String dutytype);

    Double getSingleTotalScoreByType(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype,
                                     @Param("dutytype") String dutytype,@Param("orderid")Integer orderid);

    int getCountByType(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype,
                                     @Param("dutytype") String dutytype,@Param("orderid")Integer orderid);


    List<ScoreDetail>getSingleTotalScore(@Param("mserialno") String mserialno, @Param("scoretype") String scoretype,
                                         @Param("dutytype") String dutytype,@Param("orderid")Integer orderid);


    List<ScoreDetail>selectDetailByFSerialNo(String fserialno);

    List<ScoreDetail>selectSerialNoByFSerialNo(String fserialno);

    int batchDelete(List<String>detailSerialNos);

    int batchInset(@Param("list")List<ScoreDetail>list);

    int batchUpdate(@Param("list")List<ScoreDetail>list);

    List<ScoreDetail> batchSelectDetailBySerialNo(@Param("list")List<ScoreDetail> list);

}
