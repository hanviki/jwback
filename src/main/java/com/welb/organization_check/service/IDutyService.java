package com.welb.organization_check.service;

import com.welb.organization_check.entity.Duty;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IDutyService
 * @projectName kao
 * @description: 指标管理业务层接口
 * @date 2019/5/2114:04
 */
public interface IDutyService {

    /**
     * 删除指标
     * @param dutycode
     * @return
     */
    int deleteByPrimaryKey(String dutycode);

    /**
     * 添加指标
     * @param duty
     * @return
     */
    int insertSelective(Duty duty);

    /**
     * 通过dutycode查找指标信息
     * @param dutycode
     * @return
     */
    Duty selectByPrimaryKey(String dutycode);

    /**
     * 修改指标
     * @param duty
     * @return
     */
    int updateByPrimaryKeySelective(Duty duty);

    /**
     * 查找所有指标
     * @param duty
     * @return
     */
    List<Duty> selectDutyAll(Duty duty);

    /**
     * 通过stationcode查找指标
     * @param stationcode
     * @return
     */
    List<Duty>selectDutyByStationCode(String stationcode);

    /**
     * 通过stationcode查找基础量化指标的相关数据
     * @param stationcode
     * @return
     */
    List<Duty>queryJiChu(String stationcode);
    /**
     * 通过stationcode查找关键量化指标的相关数据
     * @param stationcode
     * @return
     */
    List<Duty>queryYiBan(String stationcode);

}
