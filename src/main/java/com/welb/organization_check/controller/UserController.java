package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.constant.MapContant;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.sysBase.util.BaseController;
import com.welb.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

/**
 * @author luoyaozu
 * @title: UserController
 * @projectName kao
 * @description: 用户管理控制器
 * @date 2019/5/1614:31
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IUserService userService;
    @Resource
    IRoleService roleService;
    @Resource
    IStationService stationService;
    @Resource
    IBranchService branchService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IMonthSummaryService summaryService;
    @Resource
    IScoreService scoreService;
    @Resource
    IEvaluationReportService evaluationReportService;
    @Resource
    IEvaluateDeleteService evaluateDeleteService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IResultReportService resultReportService;
    @Resource
    IResultDetailService resultDetailService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IScoreDetailService scoreDetailService;

    /**
     * 修改用户密码
     *
     * @param user
     * @return
     */
    @RequestMapping("/updateUserPassword")
    public Object updateUserPassword(User user, String newpassword, String respassword) {
        ModelMap map = new ModelMap();
        //用户输入的密码
        String passwordMd5 = MD5.md5(user.getPassword());
        User user1 = userService.findUserByOne(user.getUsercode());
        //数据库查到的密码
        String password = user1.getPassword();
        if (!password.equals(passwordMd5)) {
            map.put("msg", "原始密码输入错误，请重新输入");
            map.put("code", 1);
        } else {
            boolean checkPassword = CheckPassword.isLetterDigit(newpassword);
            if (checkPassword) {
                if (newpassword.equals(respassword)) {//判断两次输入的密码是否一致
                    String password1 = MD5.md5(respassword);
                    user.setPassword(password1);
                    int count = userService.updateUserPassword(user);
                    userService.updateByPrimaryKeySelective(user);
                    if (count > 0) {
                        map.put("msg", "密码修改成功");
                        map.put("code", 0);
                    } else {
                        map.put("msg", "修改失败");
                        map.put("code", 1);
                    }
                } else {
                    map.put("msg", "输入的新密码不一致");
                    map.put("code", 1);
                }
            } else {
                map.put("msg", "请输入包含大小写字母及数字且在8-16位的密码");
                map.put("code", 1);
            }
        }
        return map;
    }

    /**
     * 查询所有用户 包括模糊匹配
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserAll(HttpServletRequest req, User user, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        if (userCode != null) {
            //分页
            PageHelper.startPage(pageNum, pageSize);
            List<User> users;
            try {
                users = userService.selectUserAll(user);
                PageInfo<User> pageInfo = new PageInfo<>(users);
                users = pageInfo.getList();
                users = this.handleUsersMsg(users);
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询用户成功");
                map.put("data", users);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询用户失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;

    }

    private List<User> handleUsersMsg(List<User> users) {
        if (users.size() > 0) {
            for (int i = 0; i < users.size(); i++) {
                //查找用户对应的岗位
                Station station = stationService.selectByStationCode(users.get(i).getStationcode());
                if (station != null) {
                    users.get(i).setStationname(station.getStationname());
                    users.get(i).setDepartmentcode(station.getDepartmentcode());
                    String departmentCode = users.get(i).getDepartmentcode();
                    Department department = departmentService.selectByDeptCode(departmentCode);
                    users.get(i).setDepartmentname(department.getDepartmentname());
                } else {
                    users.get(i).setStationname("");
                }
                List<Role> roles = roleService.selectRoleListByUserCode(users.get(i).getUsercode());
                users.get(i).setRolename(roles.get(0).getRolename());
                users.get(i).setRolecode(roles.get(0).getRolecode());
                //查找用户对应的支部
                Branch branch = branchService.selectByPrimaryKey(users.get(i).getBranchcode());
                if (branch != null) {
                    users.get(i).setBranchname(branch.getBranchname());
                } else {
                    users.get(i).setBranchname("");
                }

            }
        }

        return users;
    }

    //导出Excel
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,HttpServletRequest req, User user){
        ModelMap map = new ModelMap();
        String userCode = (String) req.getSession().getAttribute("usercode");
        PageData pd = this.getPageData();
        user.setStationcode(pd.getString("stationcode"));
        user.setUsername(pd.getString("username"));
        if (userCode != null) {
            try {
                List<User> users = userService.selectUserAll(user);
                users = this.handleUsersMsg(users);
                //填充ABCD类评分人信息
                List<ScoreRelationship> excelData = this.loadCategoryMsg(users);
                ExcelUtils.exportExcel(response,
                                       excelData,
                                       SCORE_RELATIONSHIP_SHEET_NAME,
                                       SCORE_RELATIONSHIP_FIRST_ROW_DATA_INDEX,
                                       SCORE_RELATIONSHIP_TEMPLATE,
                                      null);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "导出失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
    }

    private List<ScoreRelationship> loadCategoryMsg(List<User> users) {
        List<ScoreRelationship> returnList = new ArrayList<>();
        //查出全部评分数据
        List<Score> scores = scoreService.selectScoresByScorredCode(null, null);
        //填充评分人与被评分人名字
        getName(scores);
        for (User user: users) {
            ScoreRelationship scoreRelationship = new ScoreRelationship();
            scoreRelationship.setName(user.getUsername());
            scoreRelationship.setMoneyCode(user.getMoneycard());
            scoreRelationship.setDeptCode(user.getDepartmentname());
            scoreRelationship.setStation(user.getStationname());
            scoreRelationship.setTel(user.getMobile());
            //A类评分员工
            this.loadStaffAndType(scores, scoreRelationship, user.getUsercode(), CATEGORY_A);
            //B类评分员工
            this.loadStaffAndType(scores, scoreRelationship, user.getUsercode(), CATEGORY_B);
            //C类评分员工
            this.loadStaffAndType(scores, scoreRelationship, user.getUsercode(), CATEGORY_C);
            //D类评分员工
            this.loadStaffAndType(scores, scoreRelationship, user.getUsercode(), CATEGORY_D);

            //A类被评分员工
            this.loadScoredStaffMsg(scores, scoreRelationship, user.getUsercode(), CATEGORY_A);
            //B类被评分员工
            this.loadScoredStaffMsg(scores, scoreRelationship, user.getUsercode(), CATEGORY_B);
            //C类被评分员工
            this.loadScoredStaffMsg(scores, scoreRelationship, user.getUsercode(), CATEGORY_C);
            //D类被评分员工
            this.loadScoredStaffMsg(scores, scoreRelationship, user.getUsercode(), CATEGORY_D);

            returnList.add(scoreRelationship);
        }

        return returnList;
    }

    private void loadScoredStaffMsg(List<Score> scores, ScoreRelationship scoreRelationship, String userCode, String category) {
        List<Score> tempScores = scores.stream().filter(s -> s.getScorringcode().equals(userCode)).collect(Collectors.toList());
        String staffName = tempScores.stream().filter(s -> s.getScoretype().equals(category)).map(m -> m.getScorredname()).collect(Collectors.joining(DON));
        if (StringUtils.isNotEmpty(staffName)){
            switch (category){
                case CATEGORY_A:
                    scoreRelationship.setScoredStaffOfA(staffName);
                    break;
                case CATEGORY_B:
                    scoreRelationship.setScoredStaffOfB(staffName);
                    break;
                case CATEGORY_C:
                    scoreRelationship.setScoredStaffOfC(staffName);
                    break;
                case CATEGORY_D:
                    scoreRelationship.setScoredStaffOfD(staffName);
                    break;
            }
        }
    }

    private void getName(List<Score> scores) {
        List<User> allUsers = userService.selectAllUsers();
        for (Score score : scores) {
            //评分人
            List<User> scoringUserList = allUsers.stream().filter(u -> u.getUsercode().equals(score.getScorringcode())).collect(Collectors.toList());
            if (scoringUserList != null && !scoringUserList.isEmpty()) {
                User scoringUser = scoringUserList.get(0);
                score.setScorringname(scoringUser.getUsername());
            }

            //被评分人
            List<User> scoredUserList = allUsers.stream().filter(u -> u.getUsercode().equals(score.getScorredcode())).collect(Collectors.toList());
            if (scoredUserList != null && !scoredUserList.isEmpty()){
                User scoredUser = scoredUserList.get(0);
                score.setScorredname(scoredUser.getUsername());
            }
        }
    }

    private void loadStaffAndType(List<Score> scores, ScoreRelationship scoreRelationship, String userCode, String category) {
        String names = scores.stream().filter(s -> s.getScoretype().equals(category) && s.getScorredcode().equals(userCode)).map(m -> m.getScorringname()).collect(Collectors.joining(DON));
        if (StringUtils.isNotEmpty(names)){
            switch (category){
                case CATEGORY_A:
                    scoreRelationship.setRatingStaffOfA(names);
                    break;
                case CATEGORY_B:
                    scoreRelationship.setRatingStaffOfB(names);
                    break;
                case CATEGORY_C:
                    scoreRelationship.setRatingStaffOfC(names);
                    break;
                case CATEGORY_D:
                    scoreRelationship.setRatingStaffOfD(names);
                    break;
            }
        }
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object addUser(HttpServletRequest req, User user, String rolecode) {
        ModelMap map = new ModelMap();
        String state = (String) req.getSession().getAttribute("state");

        insertUser(user, rolecode, map, state);
        return map;
    }

    private void insertUser(User user, String rolecode, ModelMap map, String state) {
        try {
            user.setRoletype("0");
            String password = MD5.md5(user.getPassword());
            //对输入的密码进行加密
            user.setPassword(password);
            //默认用户状态为启用
            user.setUserstate("1");
            User user1 = userService.selectByMoneyCard(user.getMoneycard());
            if (user1 != null) {
                List<Role> roles = roleService.selectRoleListByUserCode(user1.getUsercode());
                List<String> roleCodeList = roles.stream().map(m -> m.getRolecode()).collect(Collectors.toList());
                if (roles == null || roles.isEmpty()){
                    map.put("msg", "该用户没有角色，请联系管理员添加角色");
                    map.put("code", 1);
                }else {
                    //有权限
                    if (roleCodeList.contains("100") || roleCodeList.contains("150") || roleCodeList.contains("200") || roleCodeList.contains("300") || roleCodeList.contains("50")){
                        //包含组织部任一角色,无法添加
                        map.put("msg", "该用户已存在");
                        map.put("code", 1);
                    }else {
                        user.setUsercode(user1.getUsercode());
                        //不包含组织部角色,可以添加
                        user.setRoletype("0");
                        if ("男".equals(user.getSex())) {
                            user.setSex("1");
                        } else if ("女".equals(user.getSex())) {
                            user.setSex("2");
                        }
                        int count = userService.updateByPrimaryKeySelective(user);
                        if (count > 0) {
                            addUser(user, rolecode, state);
                            map.put("msg", "添加用户成功");
                            map.put("code", 0);
                        } else {
                            map.put("msg", "添加用户失败");
                            map.put("code", 1);
                        }
                    }
                }
            } else {
                if ("男".equals(user.getSex())) {
                    user.setSex("1");
                } else if ("女".equals(user.getSex())) {
                    user.setSex("2");
                }
                int count = userService.insertSelective(user);
                if (count > 0) {
                    //添加用户的同时  季节总结管理也添加了一条数据  ，状态默认未提交
                    addUser(user, rolecode, state);
                    map.put("msg", "添加用户成功,初始密码为whuh123456");
                    map.put("code", 0);
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "添加用户失败");
            map.put("code", 1);
        }
    }

    private void addUser(User user, String rolecode, String state) throws ParseException {
        MonthSummary summary = new MonthSummary();
        String time = DateUtil.getTime();
        String usercode = user.getUsercode();
        //字符串转换成日期格式
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        summary.setPubdate(date);
        //获取当前年份
        String year = CalendarUtil.getYear();
        //获取当前月份
        String month = CalendarUtil.getMonth();
        //获取当前季度
        //String quarter = CalendarUtil.getQuarter(month);
        //当前上一个季度
        int count1 = Integer.parseInt(month.trim()) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();
        if (state.equals("1")) {
            //手动考核-查看所有季节总结
            manualAddUser(summary, usercode, year, month, count1, sysTime);

        } else {
            //自动考核-查看所有季节总结
            automaticAddUser(summary, usercode, year, count1);
        }
        //添加角色
        UserRoleKey roleKey = new UserRoleKey();
        roleKey.setRolecode(rolecode);
        roleKey.setUsercode(user.getUsercode());
        userRoleService.insertSelective(roleKey);
        if (rolecode.equals("150")) {
            user.setAratio(0.0);
            user.setBratio(0.0);
            user.setCratio(0.0);
            user.setDratio(0.0);
            userService.updateByPrimaryKeySelective(user);
        }
    }

    private void manualAddUser(MonthSummary summary, String usercode, String year, String quarter, int count1, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                addMonthSummary(summary, usercode, year, month);
            } else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticAddUser(summary, usercode, year, count1);
            }
        } else {
            if (count1 == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(count1);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                addMonthSummary(summary, usercode, year, month);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                addMonthSummary(summary, usercode, year, month);
            }
        }
    }

    private void addMonthSummary(MonthSummary summary, String usercode, String year, String month) {
        summary.setYear(year);
        summary.setMonth(month);
        //拼接个人季节唯一标识
        String serialno = summary.getYear() + "-" + summary.getMonth() + "-" + usercode;
        summary.setSerialno(serialno);
        summary.setState("0");
        summary.setEmployeecode(usercode);
        summary.setContent("");
        summary.setTitle("");
        summaryService.insertSelective(summary);
    }

    private void automaticAddUser(MonthSummary summary, String usercode, String year, int count1) {
        String month;
        if (count1 == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "4";
        } else {
            //拼接个人季节唯一标识
            month = String.valueOf(count1);

        }
        addMonthSummary(summary, usercode, year, month);
    }


    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateUser(HttpServletRequest req, User user, String rolecode) throws ParseException {
        ModelMap map = new ModelMap();
        String state = (String) req.getSession().getAttribute("state");
        try {
            List<Role> roles = roleService.selectRoleListByUserCode(user.getUsercode());
            User user1 = userService.findUserByUserCode(user.getUsercode());

            if (user.getMoneycard().equals(user1.getMoneycard())) {//修改的是同一个用户-没有更改发薪号

                user.setRoletype(user1.getRoletype());
                //对输入的密码进行加密
                user.setPassword(user1.getPassword());
                //默认用户状态为启用
                user.setUserstate(user1.getUserstate());
                user.setFlag(user1.getFlag());

                MonthSummary summary = new MonthSummary();
                String year = CalendarUtil.getYear();
                String month = CalendarUtil.getMonth();
                String quarter =month;// CalendarUtil.getQuarter(month);
                int i = Integer.parseInt(quarter) - 1;
                //获取当前系统时间
                String sysTime = DateUtil.getTime();
                if (state.equals("1")) {
                    //手动考核-查看所有季节总结
                    manualGetSerialNo(user, summary, year, quarter, i, sysTime);
                } else {
                    //自动考核-查看所有季节总结
                    automaticGetSerialNo(user, summary, year, i);
                }
                MonthSummary monthSunmmary = summaryService.selectByPrimaryKey(summary.getSerialno());
                //修改用户角色
                if (rolecode != null) {
                    if (rolecode.equals("150")) {//若是打分用户，评分系数都为0.0
                        user.setAratio(0.0);
                        user.setBratio(0.0);
                        user.setCratio(0.0);
                        user.setDratio(0.0);
                        summary.setState("");
                    } else {//若不是打分用户  则评分系数为默认评分系数
                        if (monthSunmmary.getState().equals("") || monthSunmmary.getState() == null) {
                            summary.setState("0");

                        } else {
                            summary.setState(monthSunmmary.getState());
                        }
                        user.setAratio(50.0);
                        user.setBratio(20.0);
                        user.setCratio(20.0);
                        user.setDratio(10.0);
                    }

                }
                int count = userService.updateByPrimaryKeySelective(user);
                if (count > 0) {
                    //修改打分用户为其他角色
                    UserRoleKey roleKey = new UserRoleKey();
                    roleKey.setUsercode(user.getUsercode());
                    roleKey.setRolecode(roles.get(0).getRolecode());
                    userRoleService.deleteByPrimaryKey(roleKey);//删除原有的角色
                    roleKey.setRolecode(rolecode);
                    userRoleService.insertSelective(roleKey);//新增用户-角色表
                    summaryService.updateByPrimaryKeySelective(summary);//更新季度总结表
                    map.put("msg", "修改用户成功");
                    map.put("code", 0);
                } else {
                    map.put("msg", "修改用户失败");
                    map.put("code", 0);
                }
            } else {//更改了发薪号
                User updateUser = userService.getUserByMoneyCard(user.getMoneycard());
                if (updateUser == null) {
                    deleteUser(user1);//删除原有的用户
                    insertUser(user, rolecode, map, state);//新增新用户
                    map.put("msg", "修改用户成功");
                    map.put("code", 0);
//                    userService.updateByPrimaryKeySelective(user);//将原有的用户信息替换
//                    map.put("msg", "修改用户0成功");
//                    map.put("code", 0);
                } else {
                    map.put("msg", "该用户已存在");
                    map.put("code", 1);
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "修改用户失败");
            map.put("code", 1);
        }
        return map;
    }

    private void manualGetSerialNo(User user, MonthSummary summary, String year, String quarter, int i, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                String serialNo = year + "-" + month + "-" + user.getUsercode();
                summary.setSerialno(serialNo);
            } else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticGetSerialNo(user, summary, year, i);
            }
        } else {
            if (i == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(i);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                String serialNo = year + "-" + month + "-" + user.getUsercode();
                summary.setSerialno(serialNo);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                String serialNo = year + "-" + month + "-" + user.getUsercode();
                summary.setSerialno(serialNo);
            }
        }
    }

    private void automaticGetSerialNo(User user, MonthSummary summary, String year, int i) {
        String month;
        if (i == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "12";
        } else {
            month = String.valueOf(i);

        }
        String serialNo = year + "-" + month + "-" + user.getUsercode();
        summary.setSerialno(serialNo);
    }

    /**
     * 修改权重
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/updateRatio", produces = "application/json;charset=utf-8")
    public Object updateUser(User user) {
        ModelMap map = new ModelMap();
        int count = userService.updateByPrimaryKeySelective(user);
        if (count > 0) {
            map.put("msg", "修改权重成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改权重失败");
            map.put("code", 0);
        }
        return map;
    }

    /**
     * 删除用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteUser(User user) {
        ModelMap map = new ModelMap();
        boolean flag = true;//是否删除  默认确认删除
        boolean isYDYF = false;
        try {
            //删除userrole关联用户的数据
            List<UserRoleKey> keys = userRoleService.selectUserRoleByUserCode(user.getUsercode());
            for (UserRoleKey key : keys) {
                if (key.getRolecode().equals("400") || key.getRolecode().equals("500")) {
                    //人事部
                    flag = false;
                } else if (key.getRolecode().equals("700")){
                    //医德医风
                    isYDYF = true;
                }else {
                    //组织部
                    userRoleService.deleteByPrimaryKey(key);
                }
            }
            if (flag) {
                //软删除(组织部)
                user.setFlag("1");
                userService.updateByPrimaryKeySelective(user);
            } else if (isYDYF){
                //医德医风
                user.setRoletype("2");
                userService.updateByPrimaryKeySelective(user);
            }else {
                //人事部
                user.setRoletype("1");
                userService.updateByPrimaryKeySelective(user);
            }
            map.put("msg", "刪除用户成功");
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "刪除用户失败");
            map.put("code", 1);
        }
        return map;
    }

    private void deleteScoreDetailData(ScoreFlow flow) {
        List<ScoreDetail> list = scoreDetailService.selectDetailByFSerialNo(flow.getSerialno());
        if (list.size() > 0) {
            List<String> detailIds = new ArrayList<>();
            for (ScoreDetail detail : list) {
                detailIds.add(detail.getSerialNo());
            }
            if (detailIds.size() > 0) {
                scoreDetailService.batchDelete(detailIds);
            }
        }
    }

    private void deleteResultReportData(EvaluationReport report) {
        List<ResultReport> resultReports = resultReportService.selectResultReportByEvaluationCode(report.getId());
        if (resultReports.size() > 0) {
            List<Integer> ids = new ArrayList<>();
            for (ResultReport resultReport : resultReports) {
                deleteResultDetailData(resultReport);
                ids.add(resultReport.getId());
            }
            if (ids.size() > 0) {
                resultReportService.batchDelete(ids);//批量删除resultreport数据
            }

        }
    }

    private void deleteResultDetailData(ResultReport resultReport) {
        List<ResultDetail> resultDetails = resultDetailService.selectResultDetailByReportCode(resultReport.getId());
        if (resultDetails.size() > 0) {
            List<Integer> detailIds = new ArrayList<>();
            for (ResultDetail detail : resultDetails) {
                detailIds.add(detail.getId());
            }
            if (detailIds.size() > 0) {
                resultDetailService.batchDelete(detailIds);//批量删除resultdetail数据
            }

        }
    }

    private void batchDeleteScore(List<Score> scorringscores, List<String> scorringIds) {
        if (scorringscores.size() > 0) {
            for (Score score : scorringscores) {
                scorringIds.add(score.getId());
            }
            if (scorringIds.size() > 0) {
                scoreService.batchDelete(scorringIds);
            }
        }
    }


    /**
     * 重置密码
     *
     * @param usercode
     * @return
     */
    @RequestMapping("/resetPassword")
    public Object resetPassword(String usercode) {
        ModelMap map = new ModelMap();
        User user = userService.findUserByUserCode(usercode);
        String password = INITIAL_PASSWORD;
        user.setPassword(MD5.md5(password));
        int count = userService.updateByPrimaryKeySelective(user);
        if (count > 0) {
            map.put("msg", "密码重置成功,重置的密码是 whuh123456");
            map.put("code", 0);
        } else {
            map.put("msg", "密码重置失败");
            map.put("code", 1);
        }
        return map;

    }


    /**
     * 通过发薪号查找用户信息--针对忘记密码查询使用
     *
     * @param moneycard
     * @return
     */
    @RequestMapping("/getUserByMoneyCard")
    public Object getUserByMoneyCard(String moneycard) {
        ModelMap map = new ModelMap();
        try {
            User user = userService.getUserByMoneyCard(moneycard);
            if (user == null) {
                map.put("msg", "该用户不存在");
                map.put("code", 1);
            } else if (user.getUserstate().equals("0")) {
                map.put("msg", "该用户已被停用,如有疑问,请联系管理员");
                map.put("code", 1);
            } else {
                if (!user.getMobile().equals("") || user.getMobile() != null) {//将手机号码隐藏四位
                    String phone = user.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
                    user.setMobile(phone);
                }
                map.put("msg", "查询用户成功");
                map.put("data", user);
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询用户失败");
            map.put("code", 1);
        }
        return map;
    }


    @RequestMapping("/updateNewPassword")
    public Object updateNewPassword(String checkcode, String password, String moneycard) {
        ModelMap map = new ModelMap();
        try {
            User user = userService.getUserByMoneyCard(moneycard);//查询用户信息
            String code = (String) MapContant.getMap(moneycard);//获取存放在map常量集合中的验证码
            Object startTime = MapContant.getMap(moneycard + "time");//获取存放在map常量集合中的验证码有效期
            if (startTime == null) {
                //删除map集合常量
                deleteMap(moneycard, map);
            } else {
                long endTime = System.currentTimeMillis();//当前时间戳
                int time = (int) (300 - ((endTime - (long) startTime) / 1000));//计算密码填写有效时间
                if (time > 0) {
                    if (code.equals(checkcode)) {
                        //修改新密码
                        updatePassword(password, moneycard, map, user);
                    } else {
                        map.put("msg", "验证码输入错误,请检查并重新填写验证码");
                        map.put("code", 1);
                    }
                } else {
                    deleteMap(moneycard, map);
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", LogUtil.getTrace(e));
            map.put("msg", "修改失败");
            map.put("code", 1);
        }
        return map;

    }

    private void deleteMap(String moneycard, ModelMap map) {
        MapContant.deleteMap(moneycard);
        MapContant.deleteMap(moneycard + "time");
        MapContant.deleteMap(moneycard + "message_time");
        map.put("msg", "验证码已过期,请重新发送短信");
        map.put("code", 1);
    }

    private void updatePassword(String password, String moneycard, ModelMap map, User user) {
        boolean checkPassword = CheckPassword.isLetterDigit(password);
        if (checkPassword) {
            String newPassword = MD5.md5(password);
            user.setPassword(newPassword);
            userService.updateUserPassword(user);
            userService.updateByPrimaryKeySelective(user);
            //清除验证码信息
            MapContant.deleteMap(moneycard);
            MapContant.deleteMap(moneycard + "time");
            map.put("msg", "密码修改成功");
            map.put("code", 0);

        } else {
            map.put("msg", "请输入包含大小写字母及数字且在8-16位的密码");
            map.put("code", 1);
        }
    }

}
