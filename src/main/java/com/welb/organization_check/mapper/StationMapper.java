package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.Station;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StationMapper {
    int insert(Station station);

    int insertSelective(Station record);

    Station selectByPrimaryKey(String stationcode);

    int updateByPrimaryKeySelective(Station station);

    int updateByPrimaryKey(Station station);

    int deleteByPrimaryKey(String stationcode);

    List<Station> selectStationLike(Station station);

    String selectMaxStationCode();

    List<Station>selectStationsByDeptCode(String departmentcode);


}
