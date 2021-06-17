package com.welb.vote.controller;

import com.welb.util.LogUtil;
import com.welb.vote.entity.UserDept;
import com.welb.vote.entity.UserWorker;
import com.welb.vote.entity.VoteDept;
import com.welb.vote.entity.VoteWorker;
import com.welb.vote.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author luoyaozu
 * @title: VotePageController
 * @projectName xh-360appraisal-interface
 * @description: 投票主页控制器
 * @date 2019/11/2215:35
 */
@RestController
@RequestMapping("/votePage")
public class VotePageController {
    private final Logger log = LogManager.getLogger(this.getClass());
    @Autowired
    IUserDeptService userDeptService;
    @Autowired
    IVoteUserService voteUserService;
    @Autowired
    IUserWorkerService userWorkerService;
    @Autowired
    IVoteDeptService voteDeptService;
    @Autowired
    IVoteWorkerService voteWorkerService;

    /**
     * 文明科室投票接口
     *
     * @param userId
     * @param deptId
     * @return
     */
    @RequestMapping("/voteToDept")
    public Object voteToDept(String userId, int deptId) {
        ModelMap map = new ModelMap();
        try {
            int dCount = 10;
            //获取该投票人已投的文明科室票数
            int count = userDeptService.selectCountByUserId(userId);
            if (count == dCount) {
                map.put("msg", "文明科室投票已完毕");
                map.put("code", 1);
            } else {
                UserDept userDept = userDeptService.selectDept(userId, deptId);
                if (userDept == null) {
                    //往投票人和文明科室中间表插入数据
                    addVoteDept(userId, deptId);
                    int updateCount = dCount - count - 1;//修改投票人的文明科室剩余票数
                    voteUserService.updateDCountById(updateCount, userId);
                    VoteDept voteDept = voteDeptService.selectByPrimaryKey(deptId);//获取文明科室信息
                    map.put("msg", "您已成功为"+voteDept.getName()+"投票");
                    map.put("code", 0);
                } else {
                    map.put("msg", "请勿重复投票");
                    map.put("code", 1);
                }
            }

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "投票失败");
            map.put("code", 1);
        }

        return map;
    }

    private void addVoteDept(String userId, int deptId) {
        UserDept userDept = new UserDept();
        userDept.setUserId(userId);
        userDept.setDeptId(deptId);
        userDeptService.insertSelective(userDept);//往投票人和文明科室中间表插入数据
    }


    /**
     * 医师投票接口
     *
     * @param userId
     * @param workerId
     * @return
     */
    @RequestMapping("/voteToPhysicians")
    public Object voteToPhysicians(String userId, String workerId) {
        ModelMap map = new ModelMap();
        try {
            String type = "1";
            int wCount = 10;
            //获取该投票人已投的医务工作者票数
            int count = userWorkerService.selectCountByWorkerId(userId);
            if (wCount == count) {
                map.put("msg", "医务工作者投票已完毕");
                map.put("code", 1);
            } else {

                //获取该投票人已投的医师票数
                int physiciansCount = userWorkerService.selectCountByWorkerIdAndType(userId, type);
                if (physiciansCount == 5) {
                    map.put("msg", "医师投票已完毕");
                    map.put("code", 1);
                } else {
                    UserWorker userWorker = userWorkerService.selectVoteWorker(userId, workerId);
                    if (userWorker == null) {
                        //投票成功
                        voteSuccess(userId, workerId, map, type, wCount, count);
                    } else {
                        map.put("msg", "请勿重复投票");
                        map.put("code", 1);
                    }
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "投票失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 护理投票接口
     *
     * @param userId
     * @param workerId
     * @return
     */
    @RequestMapping("/voteToNursing")
    public Object voteToNursing(String userId, String workerId) {
        ModelMap map = new ModelMap();
        try {
            String type = "2";
            int wCount = 10;
            //获取该投票人已投的医务工作者票数
            int count = userWorkerService.selectCountByWorkerId(userId);
            if (count == wCount) {
                map.put("msg", "医务工作者投票已完毕");
                map.put("code", 1);
            } else {
                //获取该投票人已投的护理票数
                int nursingCount = userWorkerService.selectCountByWorkerIdAndType(userId, type);
                if (nursingCount == 3) {
                    map.put("msg", "护理投票已完毕");
                    map.put("code", 1);
                } else {
                    UserWorker userWorker = userWorkerService.selectVoteWorker(userId, workerId);
                    if (userWorker == null) {
                        //投票成功
                        voteSuccess(userId, workerId, map, type, wCount, count);
                    } else {
                        map.put("msg", "请勿重复投票");
                        map.put("code", 1);
                    }
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "投票失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 技师投票接口
     *
     * @param userId
     * @param workerId
     * @return
     */
    @RequestMapping("/voteToTechnician")
    public Object voteToTechnician(String userId, String workerId) {
        ModelMap map = new ModelMap();
        try {
            String type = "3";
            int wCount = 10;
            //获取该投票人已投的医务工作者票数
            int count = userWorkerService.selectCountByWorkerId(userId);
            if (wCount == count) {
                map.put("msg", "医务工作者投票已完毕");
                map.put("code", 1);
            } else {
                //获取该投票人已投的技师票数
                int technicianCount = userWorkerService.selectCountByWorkerIdAndType(userId, type);
                if (technicianCount == 2) {
                    map.put("msg", "技师投票已完毕");
                    map.put("code", 1);
                } else {
                    UserWorker userWorker = userWorkerService.selectVoteWorker(userId, workerId);
                    if (userWorker == null) {
                        //投票成功
                        voteSuccess(userId, workerId, map, type, wCount, count);
                    } else {
                        map.put("msg", "请勿重复投票");
                        map.put("code", 1);
                    }
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "投票失败");
            map.put("code", 1);
        }
        return map;
    }

    private void voteSuccess(String userId, String workerId, ModelMap map, String type, int wCount, int count) {
        //投票人-医务工作者中间表添加数据
        addUserWork(userId, workerId, type);
        //修改剩余投票人的医务工作者剩余票数
        updateHCount(userId, wCount, count);
        VoteWorker voteWorker = voteWorkerService.selectByPrimaryKey(workerId);//获取医务工作者信息
        map.put("msg", "您已成功为"+voteWorker.getWorkerName()+"投票");
        map.put("code", 0);
    }

    private void addUserWork(String userId, String workerId, String type) {
        UserWorker userWorker = new UserWorker();//投票人-医务工作者中间表添加数据
        userWorker.setType(type);
        userWorker.setUserId(userId);
        userWorker.setWorkerId(workerId);
        userWorkerService.insertSelective(userWorker);
    }

    private void updateHCount(String userId, int wCount, int count) {
        int typeCount = wCount - count - 1;//修改剩余投票人的医务工作者剩余票数
        voteUserService.updateHCountById(typeCount, userId);
    }

}
