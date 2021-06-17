package com.welb.vote.controller;

import com.welb.util.LogUtil;
import com.welb.vote.entity.VoteDeptInfo;
import com.welb.vote.service.IVoteDeptInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luoyaozu
 * @title: VoteDeptInfoController
 * @projectName xh-360appraisal-interface
 * @description: 文明科室优秀事迹控制器
 * @date 2019/11/2615:17
 */
@RestController
@RequestMapping("/voteDeptInfo")
public class VoteDeptInfoController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    IVoteDeptInfoService voteDeptInfoService;

    /**
     * 通过文明科室主键查看优秀事迹
     * @param deptId
     * @return
     */
    @RequestMapping("/selectDeptInfo")
    public Object selectDeptInfo(int deptId){
        ModelMap map=new ModelMap();
        try {
            VoteDeptInfo voteDeptInfo = voteDeptInfoService.selectByPrimaryKey(deptId);
            map.put("data",voteDeptInfo);
            map.put("msg","查询成功");
            map.put("code",0);
        }catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("error",e.getMessage());
            map.put("msg","查询失败");
            map.put("code",1);
        }
        return map;
    }

}
