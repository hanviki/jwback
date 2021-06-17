package com.welb.organization_check.service;

import com.welb.organization_check.entity.Station;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IStationService
 * @projectName kao
 * @description: 岗位业务层接口
 * @date 2019/5/1715:33
 */
public interface IStationService {


    /**
     * 添加岗位
     * @param station
     * @return
     */
    int insertSelective(Station station);

    /**
     * 通过stationcode产看岗位信息
     * @param stationcode
     * @return
     */
    Station selectByStationCode(String stationcode);

    /**
     * 通过stationcode修改岗位信息
     * @param station
     * @return
     */
    int updateByPrimaryKeySelective(Station station);

    /**
     * 通过stationcode删除岗位信息
     * @param stationcode
     * @return
     */
    int deleteByPrimaryKey(String stationcode);


    /**
     * 通过岗位名称和departmentcode模糊匹配
     * @return
     */
    List<Station> selectStationByLike(Station station);

    /**
     * 通过部门code查找岗位信息
     * @param departmentcode
     * @return
     */
    List<Station>selectStationsByDeptCode(String departmentcode);


}
