package com.welb.vote.controller;

import com.welb.util.LogUtil;
import com.welb.vote.entity.VoteInfo;
import com.welb.vote.service.IVoteInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author luoyaozu
 * @title: VoteInfoController
 * @projectName xh-360appraisal-interface
 * @description: 医务工作者优秀事迹表
 * @date 2019/11/2615:01
 */
@RestController
@RequestMapping("/voteInfo")
public class VoteInfoController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    IVoteInfoService voteInfoService;

    /**
     * 通过被投票人主键/发薪号查看优秀事迹
     * @param workerId
     * @return
     */
    @RequestMapping("/selectInfo")
    public Object selectInfo(String workerId){
        ModelMap map=new ModelMap();
        try {
            VoteInfo voteInfo = voteInfoService.selectByPrimaryKey(workerId);
            map.put("data",voteInfo);
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
