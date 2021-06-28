package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.Duty;
import com.welb.organization_check.service.IDutyService;
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
 * @title: DutyController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/5/2115:38
 */
@RestController
@RequestMapping("/duty")
public class DutyController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IDutyService dutyService;

    /**
     * 查询所有指标
     *
     * @param duty
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectDutyAll(HttpServletRequest req, Duty duty, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        //pageNum:当前页  pageSize:每页总数
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<Duty> duties;
            try {
                duties = dutyService.selectDutyAll(duty);
                PageInfo<Duty> pageInfo = new PageInfo<>(duties);
                duties = pageInfo.getList();
                //获取总数据量
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询指标成功");
                map.put("data", duties);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询指标失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 添加支部
     *
     * @param duty
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object addDuty(Duty duty) {
        ModelMap map = new ModelMap();
      //  getDutyType(duty);
        int count = dutyService.insertSelective(duty);
        if (count > 0) {
            map.put("msg", "添加指标成功");
            map.put("code", 0);
        } else {
            map.put("msg", "添加指标失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 修改指标
     *
     * @param duty
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateDuty(Duty duty) {
        ModelMap map = new ModelMap();
      //  getDutyType(duty);
        int count = dutyService.updateByPrimaryKeySelective(duty);
        if (count > 0) {
            map.put("msg", "修改指标成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改指标失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 删除指标
     *
     * @param dutycode
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteDuty(String dutycode) {
        ModelMap map = new ModelMap();
        int count = dutyService.deleteByPrimaryKey(dutycode);
        if (count > 0) {
            map.put("msg", "删除指标成功");
            map.put("code", 0);
        } else {
            map.put("msg", "删除指标失败");
            map.put("code", 1);
        }
        return map;
    }

    private void getDutyType(Duty duty) {
        if (duty.getDutytype().equals("0")) {
            duty.setAscore("4");
            duty.setBscore("3");
            duty.setCscore("2");
            duty.setDscore("1");
        } else if (duty.getDutytype().equals("1")) {
            duty.setAscore("14-16");
            duty.setBscore("10-13");
            duty.setCscore("8-9");
            duty.setDscore("1-7");
        } else {
            duty.setAscore("");
            duty.setBscore("");
            duty.setCscore("");
            duty.setDscore("");
        }
    }
}
