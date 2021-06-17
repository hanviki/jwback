package com.welb.vote.controller;

import com.welb.organization_check.entity.HrpUser;
import com.welb.organization_check.service.IHrpUserService;
import com.welb.util.LogUtil;
import com.welb.util.MD5;
import com.welb.vote.entity.*;
import com.welb.vote.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.welb.sysBase.util.Constant.INITIAL_PASSWORD;

/**
 * @author luoyaozu
 * @title: VoteUserController
 * @projectName xh-360appraisal-interface
 * @description: 投票人信息控制器
 * @date 2019/11/2210:17
 */
@RestController
@RequestMapping("/voteUser")
public class VoteUserController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    IVoteUserService voteUserService;
    @Autowired
    IVoteWorkerService voteWorkerService;
    @Autowired
    IVoteDeptService voteDeptService;
    @Autowired
    IVoteRulesService voteRulesService;
    @Autowired
    IUserDeptService userDeptService;
    @Autowired
    IUserWorkerService userWorkerService;
    @Autowired
    IHrpUserService hrpUserService;

    @RequestMapping("/selectUserByUserCode")
    public Object selectUserByUserCode(String uId) {
        ModelMap map = new ModelMap();
        Map<String, Object> data = new LinkedHashMap<>();
        try {
            VoteUser voteUser = voteUserService.selectByPrimaryKey(uId);
            if (voteUser != null) {
                updateVoteUsesFlag(uId, voteUser);
                //医师集合
                List<VoteWorker> voteWorkers1 = voteWorkerService.selectAllWorkerByType("1");
                //护理集合
                List<VoteWorker> voteWorkers2 = voteWorkerService.selectAllWorkerByType("2");
                //技师集合
                List<VoteWorker> voteWorkers3 = voteWorkerService.selectAllWorkerByType("3");
                //文明科室集合
                List<VoteDept> voteDepts = voteDeptService.selectAllDept();
                //投票规则
                VoteRules voteRules = voteRulesService.selectByPrimaryKey(1);
                updateWorkerFlag(uId, voteWorkers1);
                updateWorkerFlag(uId, voteWorkers2);
                updateWorkerFlag(uId, voteWorkers3);
                updateDeptFlag(uId, voteDepts);
                data.put("voteUser", voteUser);
                data.put("voteWorkers1", voteWorkers1);
                data.put("voteWorkers2", voteWorkers2);
                data.put("voteWorkers3", voteWorkers3);
                data.put("voteDepts", voteDepts);
                data.put("voteRules", voteRules);
                map.put("data", data);
                map.put("msg", "查询成功");
                map.put("code", 0);
            } else {
                map.put("msg", "该投票人不存在，请检查编号输入是否正确");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 当批量修改科室和医务工作者总票数时 ，状态改为2
     * flag 投票状态 1-否  2-是
     *
     * @param uId
     * @param voteUser
     */
    private void updateVoteUsesFlag(String uId, VoteUser voteUser) {
        if (voteUser.gethCount() == 0 && voteUser.getdCount() == 0) {
            if (voteUser.getFlag().equals("1")) {
                batchUpdateDeptCount(uId);//批量修改文明科室总票数
                batchUpdateWorkerCount(uId);//批量修改医务工作者总票数
                voteUser.setFlag("2");
                voteUserService.updateByPrimaryKeySelective(voteUser);

            }
        }
    }

    /**
     * 当投票人给某个科室投票后，状态更改为2，否则为1
     * flag 投票状态 1-否  2-是
     *
     * @param uId
     * @param voteDepts
     */
    private void updateDeptFlag(String uId, List<VoteDept> voteDepts) {
        for (VoteDept dept : voteDepts) {
            UserDept userDept = userDeptService.selectDept(uId, dept.getId());
            if (userDept != null) {
                dept.setFlag("2");
            } else {
                dept.setFlag("1");
            }
        }
    }

    /**
     * 当投票人给某个医务工作者投票后，状态更改为2，否则为1
     * flag 投票状态 1-否  2-是
     *
     * @param uId
     * @param voteWorkers
     */
    private void updateWorkerFlag(String uId, List<VoteWorker> voteWorkers) {
        for (VoteWorker worker : voteWorkers) {
            UserWorker userWorker = userWorkerService.selectVoteWorker(uId, worker.getWorkerId());
            if (userWorker != null) {
                worker.setFlag("2");
            } else {
                worker.setFlag("1");
            }
        }
    }

    private void batchUpdateWorkerCount(String uId) {
        List<String> workerIds = new ArrayList<>();
        List<UserWorker> workers = userWorkerService.selectWorkerByWorkerId(uId);
        for (UserWorker worker : workers) {
            workerIds.add(worker.getWorkerId());
        }
        voteWorkerService.batchUpdate(workerIds);
    }

    private void batchUpdateDeptCount(String uId) {
        List<Integer> ids = new ArrayList<>();
        List<UserDept> depts = userDeptService.selectDeptByUserId(uId);
        for (UserDept dept : depts) {
            ids.add(dept.getDeptId());
        }
        voteDeptService.batchUpdate(ids);
    }

    /**
     * 投票登录接口
     *
     * @param uId
     * @return
     */
    @RequestMapping("/login")
    public Object login(String uId) {
        ModelMap map = new ModelMap();
        if (uId.length() == 16) {
            VoteUser voteUser = voteUserService.selectByPrimaryKey(uId);
            if (voteUser != null) {
                map.put("data", voteUser);
                map.put("msg", "登陆成功");
                map.put("code", 0);
            } else {
                map.put("msg", "账号错误");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "账号错误");
            map.put("code", 1);
        }

        return map;
    }

    /**
     * 修改投票用户密码
     *
     * @return
     */
    @RequestMapping("updatePassword")
    public Object updatePassword() {
        ModelMap map = new ModelMap();
        int count = 0;
        try {
            List<VoteUser> voteUsers = voteUserService.selectAll();
            if (voteUsers.size() > 0) {
                for (VoteUser user : voteUsers) {
                    if (user.getuIdCard() != null) {
                        if (user.getuPassword().equals(INITIAL_PASSWORD)) {
                            String password = user.getuIdCard().substring(user.getuIdCard().length() - 6);
                            user.setuPassword(password);
                            voteUserService.updateByPrimaryKeySelective(user);
                            count++;
                        }
                    }
                }
                map.put("count", count);
                map.put("msg", "修改成功");
                map.put("code", 0);
            }
            map.put("msg", "修改成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.getStackTrace();
            map.put("error", e.getStackTrace());
            map.put("msg", "异常");
            map.put("code", 1);
            map.put("count", count);
        }
        return map;
    }


    /**
     * 与人事部用户数据比对，并进行添加
     *
     * @return
     */
    @RequestMapping("/addVoteUser")
    public Object addVoteUser() {
        ModelMap map = new ModelMap();
        int count=0;
        try {
            List<HrpUser> hrpUsers = hrpUserService.selectAll();
            if (hrpUsers.size() > 0) {
                for (HrpUser user : hrpUsers) {
                    String uId = MD5.md5(user.getuId());
                    VoteUser voteUser = voteUserService.selectByPrimaryKey(uId);
                    count++;
                    System.err.println("已执行"+count+"次");
                    if (voteUser == null) {
                        addVoteUser(user, uId);
                    }
                }
                map.put("msg", "成功");
                map.put("code", 0);
            }

        } catch (Exception e) {
            map.put("error", e.getStackTrace());
            map.put("msg", "异常");
            map.put("code", 1);
        }
        return map;
    }

    private void addVoteUser(HrpUser user, String uId) {
        VoteUser voteUser = new VoteUser();
        voteUser.setuId(uId);
        voteUser.setuPassword(user.getuIdCard().substring(user.getuIdCard().length() - 6));
        voteUser.setuName(user.getuName());
        voteUser.setuImg(user.getuImg());
        voteUser.setuSex(user.getuSex());
        voteUser.setuBirth(user.getuBirth());
        voteUser.setuHomeAddress(user.getuHomeAddress());
        voteUser.setuNation(user.getuNation());
        voteUser.setuNativePlace(user.getuNativePlace());
        voteUser.setuIdCard(user.getuIdCard());
        voteUser.setuJobTime(user.getuJobTime());
        voteUser.setuOldId(user.getuOldId());
        voteUser.setuHospitalTime(user.getuHospitalTime());
        voteUser.setuJobNumber(user.getuJobNumber());
        voteUser.setuEmail(user.getuEmail());
        voteUser.setuPhone(user.getuPhone());
        voteUser.setuTitleLevel(user.getuTitleLevel());
        voteUser.setuTechnicalPosition1(user.getuTechnicalPosition1());
        voteUser.setuPositionTime1(user.getuPositionTime1());
        voteUser.setuTechnicalPosition2(user.getuTechnicalPosition2());
        voteUser.setuPositionTime2(user.getuPositionTime2());
        voteUser.setuEmploymentUnit(user.getuEmploymentUnit());
        voteUser.setuCheckDepartment(user.getuCheckDepartment());
        voteUser.setuStatisticDepartment(user.getuStatisticDepartment());
        voteUser.setuHisNumber(user.getuHisNumber());
        voteUser.setuUpdateStatus(user.getuUpdateStatus());
        voteUser.setuActivateStatus(user.getuActivateStatus());
        user.setuCheckDataStatus(voteUser.getuCheckDataStatus());
        voteUser.setuTel(user.getuTel());
        voteUser.setuDegree(user.getuDegree());
        voteUser.setuEdu(user.getuEdu());
        voteUser.sethCount(10);
        voteUser.setdCount(10);
        voteUser.setFlag("10");
        voteUserService.insertSelective(voteUser);
    }
}
