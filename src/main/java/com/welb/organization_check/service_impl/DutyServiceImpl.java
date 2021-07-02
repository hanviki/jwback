package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.Duty;
import com.welb.organization_check.mapper.DutyMapper;
import com.welb.organization_check.service.IDutyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DutyServiceImpl
 * @projectName kao
 * @description: 指标管理业务层接口的实现类
 * @date 2019/5/2114:20
 */
@Service
@Transactional
public class DutyServiceImpl implements IDutyService {
    @Resource
    DutyMapper dutyMapper;

    @Override
    public int deleteByPrimaryKey(String dutycode) {
        return dutyMapper.deleteByPrimaryKey(dutycode);
    }

    @Override
    public int insertSelective(Duty duty) {
        //实现dutycode自增
        String dutyCode = dutyMapper.selectMaxDutyCode();
        if (dutyCode==null){
            duty.setDutycode("100");
        }else {
            int num = Integer.parseInt(dutyCode.trim());
            num++;
            String dutycode = String.valueOf(num);
            duty.setDutycode(dutycode);
        }
        return dutyMapper.insertSelective(duty);
    }

    @Override
    public Duty selectByPrimaryKey(String dutycode) {

        return dutyMapper.selectByPrimaryKey(dutycode);
    }

    @Override
    public int updateByPrimaryKeySelective(Duty duty) {

        return dutyMapper.updateByPrimaryKeySelective(duty);
    }

    @Override
    public List<Duty> selectDutyAll(Duty duty) {
        return dutyMapper.selectDutyAll(duty);
    }

    @Override
    public List<Duty> selectDutyByStationCode(String stationcode) {
        return dutyMapper.selectDutyByStationCode(stationcode);
    }

    @Override
    public List<Duty> queryJiChu(String stationcode) {
        return dutyMapper.queryJiChu(stationcode);
    }
    @Override
    public List<Duty> queryYiBan(String stationcode) {
        return dutyMapper.queryYiBan(stationcode);
    }

    @Override
    public List<Duty> queryDutyByType(String dutyType,String stationcode) {
        return dutyMapper.queryDutyByType(dutyType,stationcode);
    }
}
