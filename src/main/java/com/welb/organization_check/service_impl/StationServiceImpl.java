package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Station;
import com.welb.organization_check.mapper.StationMapper;
import com.welb.organization_check.service.IStationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: StationServiceImpl
 * @projectName kao
 * @description: 岗位业务层接口的实现类
 * @date 2019/5/1716:34
 */
@Service
@Transactional
public class StationServiceImpl implements IStationService {
    @Resource
    StationMapper stationMapper;

    @Override
    public int insertSelective(Station station) {
        //实现stationcode自增
        String stationCode = stationMapper.selectMaxStationCode();
        if (stationCode==null){
            station.setStationcode("100");

        }else {
            int num = Integer.parseInt(stationCode.trim());
            num++;
            String code = String.valueOf(num);
            station.setStationcode(code);
        }
        return stationMapper.insertSelective(station);
    }

    @Override
    public Station selectByStationCode(String stationcode) {
        return stationMapper.selectByPrimaryKey(stationcode);
    }

    @Override
    public int updateByPrimaryKeySelective(Station station) {
        return stationMapper.updateByPrimaryKeySelective(station);
    }

    @Override
    public int deleteByPrimaryKey(String stationcode) {
        return stationMapper.deleteByPrimaryKey(stationcode);
    }


    @Override
    public List<Station> selectStationByLike(Station station) {
        return stationMapper.selectStationLike(station);
    }

    @Override
    public List<Station> selectStationsByDeptCode(String departmentcode) {
        return stationMapper.selectStationsByDeptCode(departmentcode);
    }
}
