package com.welb.organization_check.controller;

import com.welb.organization_check.entity.HrpUser;
import com.welb.organization_check.service.IHrpUserService;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: HrpUserController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/2910:56
 */
@RestController
@RequestMapping("/hrpUser")
public class HrpUserController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IHrpUserService hrpUserService;

    /**
     * 通过主键(发薪号)查找hap用户数据
     *
     * @param uId
     * @return
     */
    @RequestMapping("/findHrpUserById")
    public Object findHrpUserById(String uId) {
        ModelMap map = new ModelMap();
        try {
            HrpUser hrpUser = hrpUserService.selectByPrimaryKey(uId);
            if (hrpUser == null) {
                map.put("msg", "暂无数据");
                map.put("code", 0);
            } else {
                map.put("data", hrpUser);
                map.put("msg", "查询成功");
                map.put("code", 0);
            }

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }

        return map;


    }
}
