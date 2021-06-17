package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.Duty;
import com.welb.organization_check.entity.Station;
import com.welb.organization_check.service.IDutyService;
import com.welb.organization_check.service.IStationService;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author luoyaozu
 * @title: StationController
 * @projectName kao
 * @description: 岗位控制器
 * @date 2019/5/1716:47
 */
@RestController
@RequestMapping("/station")
public class StationController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IStationService stationService;
    @Resource
    IDutyService dutyService;

    /**
     * 查询所有，包含条件模糊查询
     *
     * @param station
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object findStationAll(HttpServletRequest req, int pageNum, int pageSize, Station station) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {

            //pageNum:当前所在的页面  pageSize:每页有多少条数据
            PageHelper.startPage(pageNum, pageSize);
            try {
                List<Station> stations = stationService.selectStationByLike(station);
                PageInfo<Station> pageInfo = new PageInfo<>(stations);
                List<Station> stationList = pageInfo.getList();
                //数据总量
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", stationList);
                map.put("msg", "查询岗位成功 ");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询岗位失败 ");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 添加岗位
     *
     * @param station
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object addStation(Station station) {
        ModelMap map = new ModelMap();
        int count = stationService.insertSelective(station);
        if (count > 0) {
            map.put("msg", "添加岗位成功");
            map.put("code", 0);
        } else {
            map.put("msg", "添加岗位失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 修改岗位信息
     *
     * @param station
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateStation(Station station) {
        ModelMap map = new ModelMap();
        int count = stationService.updateByPrimaryKeySelective(station);
        if (count > 0) {
            map.put("msg", "修改岗位成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改岗位失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 通过stationcode删除岗位信息
     *
     * @param stationcode
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteStation(String stationcode) {
        ModelMap map = new ModelMap();
        //删除与岗位关联的指标
        List<Duty> duties = dutyService.selectDutyByStationCode(stationcode);
        for (Duty duty : duties
        ) {
            dutyService.deleteByPrimaryKey(duty.getDutycode());
        }
        int count = stationService.deleteByPrimaryKey(stationcode);
        if (count > 0) {
            map.put("msg", "删除岗位成功");
            map.put("code", 0);
        } else {
            map.put("msg", "删除岗位失败");
            map.put("code", 1);
        }
        return map;
    }

}
