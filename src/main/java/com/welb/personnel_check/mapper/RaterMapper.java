package com.welb.personnel_check.mapper;

import com.welb.personnel_check.entity.Rater;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RaterMapper {
    int deleteByPrimaryKey(String ratercode);

    int insertSelective(Rater rater);

    Rater selectByPrimaryKey(String ratercode);

    Rater selectByMoneyCard(String moneycard);

    int updateByPrimaryKeySelective(Rater rater);

    List<Rater> selectAllRater(Rater rater);

    List<Rater> selectList();

    String selectMaxCode();

    int deleteRaterByScorringCode(String scorringcode);

    List<Rater>selectDepartList();

    Rater  selectRaterByDept(String department);
}
