package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Duty;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface DutyMapper {
    int deleteByPrimaryKey(String dutycode);

    int insert(Duty record);

    int insertSelective(Duty record);

    Duty selectByPrimaryKey(String dutycode);

    int updateByPrimaryKeySelective(Duty record);

    int updateByPrimaryKey(Duty record);

    List<Duty>selectDutyAll(Duty duty);

    String selectMaxDutyCode();

    List<Duty>selectDutyByStationCode(String stationcode);

    List<Duty>queryJiChu(String stationcode);

    List<Duty>queryYiBan(String stationcode);
}
