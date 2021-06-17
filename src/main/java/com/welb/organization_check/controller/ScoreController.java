package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ScoreController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/5/2817:48
 */
@RestController
@RequestMapping("/score")
public class ScoreController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    IScoreService scoreService;
    @Resource
    IUserService userService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IRoleService roleService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IScoreHistoryService historyService;

    /**
     * 通过评分人code查找被评分人关系数据
     *
     * @param scorredcode
     * @param scoretype
     * @param username
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/listScorred", produces = "application/json;charset=utf-8")
    public Object selectListScorred(HttpServletRequest req, String scorredcode, String scoretype, String stationcode,
                                    String username, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<Score> scores;
            try {
                if (!username.equals("") && !stationcode.equals("")) {
                    scores = scoreService.selectScoresByscorredAndUser(scorredcode, scoretype, stationcode, username);
                    getList(map, scores);
                } else if (!username.equals("") && stationcode.equals("")) {
                    scores = scoreService.selectScoresByscorredAndUserName(scorredcode, scoretype, username);
                    getList(map, scores);
                } else if (username.equals("") && !stationcode.equals("")) {
                    scores = scoreService.selectScoresByscorredAndUserStationCode(scorredcode, scoretype, stationcode);
                    getList(map, scores);
                } else {
                    scores = scoreService.selectScoresByScorredCode(scorredcode, scoretype);
                    getList(map, scores);
                }
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 获取集合通用方法
     *
     * @param map
     * @param scores
     */
    private void getList(ModelMap map, List<Score> scores) {
        PageInfo<Score> pageInfo = new PageInfo<>(scores);
        scores = pageInfo.getList();
        getName(scores);
        map.put("totalPages", pageInfo.getTotal());
        map.put("msg", "查询成功");
        map.put("data", scores);
    }

    /**
     * 通过被评分人code查找评分人关系数据
     *
     * @param scorredcode
     * @param scoretype
     * @param username
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/listScorring", produces = "application/json;charset=utf-8")
    public Object selectListScorring(HttpServletRequest req, String scorredcode, String scoretype, String stationcode,
                                     String username, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            PageHelper.startPage(pageNum, pageSize);
            List<Score> scores;
            try {
                if (!username.equals("") && !stationcode.equals("")) {
                    scores = scoreService.selectScoresByScorringAndUser(scorredcode, scoretype, stationcode, username);
                    getList(map, scores);
                    map.put("code", 0);
                } else if (!username.equals("") && stationcode.equals("")) {
                    scores = scoreService.selectScoresByScorringAndUserName(scorredcode, scoretype, username);
                    getList(map, scores);
                    map.put("code", 0);
                } else if (username.equals("") && !stationcode.equals("")) {
                    scores = scoreService.selectScoresByScorringAndUserStationCode(scorredcode, scoretype, stationcode);
                    getList(map, scores);
                    map.put("code", 0);
                } else {
                    scores = scoreService.selectScoresByScorringCode(scorredcode, scoretype);
                    getList(map, scores);
                    map.put("code", 0);
                }
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 获取用户姓名通用方法
     *
     * @param scores
     */

    private void getName(List<Score> scores) {
        for (Score score1 : scores) {
            User user = userService.findUserByUserCode(score1.getScorredcode());
            if (user == null) {
            } else {
                score1.setScorredname(user.getUsername());
                Station station = stationService.selectByStationCode(user.getStationcode());
                if (station != null) {//查找被评分人的岗位和部门
                    score1.setStationcode2(station.getStationcode());
                    score1.setStationname2(station.getStationname());
                    Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
                    if (department != null) {
                        score1.setDepartmentcode2(department.getDepartmentcode());
                        score1.setDepartmentname2(department.getDepartmentname());
                    } else {
                        score1.setDepartmentcode2("");
                        score1.setDepartmentname2("");
                    }
                } else {
                    score1.setStationcode1("");
                    score1.setStationname1("");
                }

            }
            User user1 = userService.findUserByUserCode(score1.getScorringcode());
            if (user1 == null) {
            } else {
                score1.setScorringname(user1.getUsername());
                Station station = stationService.selectByStationCode(user1.getStationcode());
                if (station != null) {//查找评分人的岗位和部门
                    score1.setStationcode1(station.getStationcode());
                    score1.setStationname1(station.getStationname());
                    Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
                    if (department != null) {
                        score1.setDepartmentcode1(department.getDepartmentcode());
                        score1.setDepartmentname1(department.getDepartmentname());
                    } else {
                        score1.setDepartmentcode1("");
                        score1.setDepartmentname1("");
                    }
                } else {
                    score1.setStationcode1("");
                    score1.setStationname1("");
                }
            }

        }
    }

    /**
     * 修改评分关系数据
     *
     * @param score
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateScore(HttpServletRequest req, Score score) {
        ModelMap map = new ModelMap();
        String state = (String) req.getSession().getAttribute("state");
        //查找是否有相同的评分人
        Score selectScoreByCode = scoreService.selectTypeByCode(score.getScorredcode(), score.getScorringcode());
        //查找要修改之前的数据
        Score scoreById = scoreService.selectByPrimaryKey(score.getId());
        if (score.getScorringcode().equals(score.getScorredcode())) {
            map.put("msg", "不能添加自己作为评分人");
            map.put("code", 1);
        } else if (selectScoreByCode != null) {
            if (selectScoreByCode.getScorringcode().equals(score.getScorringcode())) {
                if (selectScoreByCode.getScoretype().equals(score.getScoretype())) {
                    getScorringInfo(score, map, selectScoreByCode);
                } else {
                    if (scoreById.getScorringcode().equals(score.getScorringcode())) {
                        judgeScoreIsNull(state, score, map, selectScoreByCode);
                        int count = scoreService.updateByPrimaryKeySelective(score);
                        updateSuccess(map, count, "修改数据成功", "修改数据失败");
                    } else {
                        getScorringInfo(score, map, selectScoreByCode);
                    }
                }
            } else {
                judgeScoreIsNull(state, score, map, selectScoreByCode);
                int count = scoreService.updateByPrimaryKeySelective(score);
                updateSuccess(map, count, "修改数据成功", "修改数据失败");
            }
        } else {
            int count = scoreService.updateByPrimaryKeySelective(score);
            updateSuccess(map, count, "修改数据成功", "修改数据失败");
        }
        return map;

    }

    private void getScorringInfo(Score score, ModelMap map, Score selectScoreByCode) {
        User user = userService.findUserByUserCode(score.getScorringcode());
        String moneycard = user.getMoneycard();
        String username = user.getUsername();
        String type = selectScoreByCode.getScoretype();
        map.put("msg", username + "(" + moneycard + "-" + type + ")" + "已被选择为其他类评分人，请重新选择");
        map.put("code", 1);
    }

    private void judgeScoreIsNull(String state, Score score, ModelMap map, Score selectScoreByCode){
        int count = scoreService.updateByPrimaryKeySelective(score);
        String year = CalendarUtil.getYear();
        String month = CalendarUtil.getMonth();
        String quarter = CalendarUtil.getQuarter(month);
        int i = Integer.parseInt(quarter.trim()) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();
        if (state.equals("1")) {
            //手动考核-查看所有季节总结
            manualUpdateScore(score, map, selectScoreByCode, count, year, quarter, i, sysTime);
        } else {
            //自动考核-修改评分关系
            automaticUpdateScore(score, map, selectScoreByCode, count, year, i);
        }

    }

    private void manualUpdateScore(Score score, ModelMap map, Score selectScoreByCode, int count, String year, String quarter, int i, String sysTime) {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            try {
                if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                    //开始新的季度考核
                    month = quarter;
                    getScoreFlowType(score, map, selectScoreByCode, count, year, month);
                }else {
                    //未到达指定考核时间，仍展示上一季度数据
                    automaticUpdateScore(score, map, selectScoreByCode, count, year, i);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (i == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "4";
            } else {
                month = String.valueOf(i);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            try {
                if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                    getScoreFlowType(score, map, selectScoreByCode, count, year, month);
                } else {
                    int lastMonth = Integer.parseInt(month) - 1;
                    if (lastMonth==0){
                        lastMonth=4;
                        int lastYear = Integer.parseInt(year)-1;
                        year=String.valueOf(lastYear);
                    }
                    month = String.valueOf(lastMonth);
                    getScoreFlowType(score, map, selectScoreByCode, count, year, month);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void getScoreFlowType(Score score, ModelMap map, Score selectScoreByCode, int count, String year, String month) {
        String scorredcode = selectScoreByCode.getScorredcode();
        String scorringcode = selectScoreByCode.getScorringcode();
        String mserialno = year + "-" + month + "-" + scorredcode;
        updateScoreFlowType(map, score, count, scorringcode, mserialno);
    }

    private void automaticUpdateScore(Score score, ModelMap map, Score selectScoreByCode, int count, String year, int i) {
        String month;
        if (i == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "4";
        } else {
            month = String.valueOf(i);
        }
        getScoreFlowType(score, map, selectScoreByCode, count, year, month);
    }

    /**
     * 添加各类评分人数据
     *
     * @return
     */
    @RequestMapping(value = "/addScorring", produces = "application/json;charset=utf-8")
    public Object addScorrring(Score score, String fullscorringcode, String scoretype) {
        ModelMap map = new ModelMap();
        try {
            List<Role> roles = roleService.selectRoleListByUserCode(score.getScorredcode());
            if (!roles.get(0).getRolecode().equals("150")) {
                int flag = 1;
                boolean temp = false;
                StringBuilder sb = new StringBuilder();
                String[] scorringcodes = fullscorringcode.split(",");
                List<String> scorringcode = new ArrayList<>();
                Score score1 = new Score();
                int counts = 0;
                for (int i = 0; i < scorringcodes.length; i++) {
                    score1.setScorredcode(score.getScorredcode());
                    score1.setScoretype(scoretype);
                    score1.setScorringcode(scorringcodes[i]);
                    Score selectScoreByCode = scoreService.selectTypeByCode(score1.getScorredcode(), score1.getScorringcode());
                    if (score1.getScorringcode().equals(score1.getScorredcode())) {
                        flag = 1;
                        break;
                    }
                    if (selectScoreByCode != null) {
                        temp = true;
                        User user = userService.findUserByUserCode(score1.getScorringcode());
                        String moneycard = user.getMoneycard();
                        String username = user.getUsername();
                        String type = selectScoreByCode.getScoretype();
                        sb.append(username + "(" + moneycard + "-" + type + ")").append(";");
                    } else {
                        flag = 3;
                        scorringcode.add(score1.getScorringcode());
                    }
                }
                if (flag == 1) {
                    map.put("msg", "不能添加自己作为评分人");
                    map.put("code", 1);
                }
                if (temp) {
                    sb.append("以上员工已被选择为其他类评分人，请重新选择");
                    map.put("msg", sb);
                    map.put("code", 1);
                } else {
                    if (scorringcode.size() == scorringcodes.length) {
                        Score newScore = new Score();
                        for (int i = 0; i < scorringcode.size(); i++) {
                            newScore.setScorredcode(score.getScorredcode());
                            newScore.setScoretype(score.getScoretype());
                            newScore.setScorringcode(scorringcode.get(i));
                            int count = scoreService.insertSelective(newScore);
                            //修改评分状态
                            ScoreHistory history = historyService.selectScoreHistoryByUserCode(newScore.getScorringcode());
                            if (history!=null){
                                if (history.getScorestatus().equals("3")){
                                    history.setScorestatus("2");
                                    historyService.updateByPrimaryKeySelective(history);
                                }
                            }
                            counts += count;
                        }
                        if (counts > 0) {
                            map.put("msg", "添加" + scoretype + "类评分人成功");
                            map.put("code", 0);
                        } else {
                            map.put("msg", "添加" + scoretype + "类评分人失败");
                            map.put("code", 1);
                        }
                    }
                }
            } else {
                map.put("msg", "该用户是打分人员,不能添加评分人员");
                map.put("code", 1);
            }

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
        return map;
    }

    /**
     * 添加被评分人数据
     *
     * @return
     */
    @RequestMapping(value = "/addScorred", produces = "application/json;charset=utf-8")
    public Object addScored(Score score, String fullscorredcode, String scoretype) {
        ModelMap map = new ModelMap();
        String[] scorredcodes = fullscorredcode.split(",");
        List<String> scorredcode = new ArrayList<>();
        Score score1 = new Score();
        int flag = 1;
        boolean temp = false;
        StringBuilder sb = new StringBuilder();
        int counts = 0;
        for (int i = 0; i < scorredcodes.length; i++) {
            score1.setScorredcode(scorredcodes[i]);
            score1.setScoretype(scoretype);
            score1.setScorringcode(score.getScorringcode());
            Score selectScoreByCode = scoreService.selectTypeByCode(score1.getScorredcode(), score1.getScorringcode());
            if (score1.getScorringcode().equals(score1.getScorredcode())) {
                flag = 1;
                break;
            }
            if (selectScoreByCode != null) {
                temp = true;
                User user = userService.findUserByUserCode(score1.getScorredcode());
                String moneycard = user.getMoneycard();
                String username = user.getUsername();
                String type = selectScoreByCode.getScoretype();
                sb.append(username + "(" + moneycard + "-" + type + ")").append(";");
            } else {
                scorredcode.add(score1.getScorredcode());
            }
        }
        if (flag == 1) {
            map.put("msg", "不能添加自己作为被评分人");
            map.put("code", 1);
        }
        if (temp) {
            sb.append("以上员工已被选择为其他类被评分人，请重新选择");
            map.put("msg", sb);
            map.put("code", 1);
        } else {
            if (scorredcode.size() == scorredcodes.length) {
                Score newScore = new Score();
                for (int i = 0; i < scorredcode.size(); i++) {
                    newScore.setScorringcode(score.getScorringcode());
                    newScore.setScoretype(score.getScoretype());
                    newScore.setScorredcode(scorredcode.get(i));
                    int count = scoreService.insertSelective(newScore);
                    counts += count;
                }
                if (counts > 0) {
                    //修改评分状态
                    ScoreHistory history = historyService.selectScoreHistoryByUserCode(score.getScorringcode());
                    if (history!=null){
                        if (history.getScorestatus().equals("3")){
                            history.setScorestatus("2");
                            historyService.updateByPrimaryKeySelective(history);
                        }
                    }
                    map.put("msg", "添加" + scoretype + "类被评分人成功");
                    map.put("code", 0);
                } else {
                    map.put("msg", "添加" + scoretype + "类被评分人失败");
                    map.put("code", 1);
                }
            }

        }
        return map;
    }


    /**
     * 删除评分关系数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteScore(String id) {
        ModelMap map = new ModelMap();
        Score score = scoreService.selectByPrimaryKey(id);
        ScoreFlow flow = new ScoreFlow();
        flow.setScoretype(score.getScoretype());
        flow.setScoredcode(score.getScorredcode());
        flow.setScorringcode(score.getScorringcode());
        List<ScoreFlow> scoreFlows = flowService.selectScoreByCodeAndType(flow);
        if (scoreFlows.size() > 0) {
            for (ScoreFlow scoreFlow : scoreFlows) {
                flowService.deleteByPrimaryKey(scoreFlow.getSerialno());
            }
        }
        int count = scoreService.deleteByPrimaryKey(id);
        updateSuccess(map, count, "删除数据成功", "删除数据失败");
        return map;

    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/batchDelete")
    public Object batchDelete(String ids) {
        ModelMap map = new ModelMap();
        try {
            String[] split = ids.split(",");
            for (int i = 0; i < split.length; i++) {
                Score score = scoreService.selectByPrimaryKey(split[i]);
                ScoreFlow flow = new ScoreFlow();
                flow.setScoretype(score.getScoretype());
                flow.setScoredcode(score.getScorredcode());
                flow.setScorringcode(score.getScorringcode());
                List<ScoreFlow> scoreFlows = flowService.selectScoreByCodeAndType(flow);
                if (scoreFlows.size() > 0) {
                    for (ScoreFlow scoreFlow : scoreFlows) {
                        flowService.deleteByPrimaryKey(scoreFlow.getSerialno());
                    }
                }
                scoreService.deleteByPrimaryKey(split[i]);
            }
            map.put("msg", "批量删除成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "批量删除失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 修改评分关系数据
     *
     * @param score
     * @return
     */
    @RequestMapping(value = "/updateScore", produces = "application/json;charset=utf-8")
    public Object updateScore1(HttpServletRequest req, Score score) {
        ModelMap map = new ModelMap();
        String state = (String) req.getSession().getAttribute("state");
        Score selectScoreByCode = scoreService.selectTypeByCode(score.getScorredcode(), score.getScorringcode());
        Score scoreById = scoreService.selectByPrimaryKey(score.getId());
        if (score.getScorringcode().equals(score.getScorredcode())) {
            map.put("msg", "不能添加自己作为被评分人");
            map.put("code", 1);
        } else {
            if (selectScoreByCode != null) {
                if (selectScoreByCode.getScorredcode().equals(score.getScorredcode())) {
                    if (selectScoreByCode.getScoretype().equals(score.getScoretype())) {
                        getScorredInfo(score, map, selectScoreByCode);
                    } else {
                        if (scoreById.getScorredcode().equals(score.getScorredcode())) {
                            judgeScoreIsNull(state, score, map, selectScoreByCode);
                            int count = scoreService.updateByPrimaryKeySelective(score);
                            updateSuccess(map, count, "修改数据成功", "修改数据失败");
                        } else {
                            getScorredInfo(score, map, selectScoreByCode);

                        }
                    }
                } else {
                    judgeScoreIsNull(state, score, map, selectScoreByCode);
                    int count = scoreService.updateByPrimaryKeySelective(score);
                    updateSuccess(map, count, "修改数据成功", "修改数据失败");
                }
            } else {
                int count = scoreService.updateByPrimaryKeySelective(score);
                updateSuccess(map, count, "修改数据成功", "修改数据失败");
            }
        }
        return map;

    }

    private void getScorredInfo(Score score, ModelMap map, Score selectScoreByCode) {
        User user = userService.findUserByUserCode(score.getScorredcode());
        String moneycard = user.getMoneycard();
        String username = user.getUsername();
        String type = selectScoreByCode.getScoretype();
        map.put("msg", username + "(" + moneycard + "-" + type + ")" + "已被选择为其他类评分人，请重新选择");
        map.put("code", 1);
    }


    private void updateScoreFlowType(ModelMap map, Score score, int count, String scorringcode, String mserialno) {
        try {
            ScoreFlow flow = flowService.selectFlowByMserialNoAndScorringCode(mserialno, scorringcode);
            if (flow != null) {
                flow.setScoretype(score.getScoretype());
                flowService.updateByPrimaryKeySelective(flow);
                updateSuccess(map, count, "修改数据成功", "修改数据失败");
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "修改失败,被评分人不能有两个或两个以上相同的评分人,请检查并删除其中一个或多个 相同的评分人");
            map.put("code", 1);
        }
    }

    private void updateSuccess(ModelMap map, int count, String 修改数据成功, String 修改数据失败) {
        if (count > 0) {
            map.put("msg", 修改数据成功);
            map.put("code", 0);
        } else {
            map.put("msg", 修改数据失败);
            map.put("code", 1);
        }
    }


    /**
     * 刪除数据库残留的垃圾数据
     *
     * @return
     */
    @RequestMapping("/findAll")
    public Object findAll() {
        ModelMap map = new ModelMap();
        try {
            List<Score> scoreAll = scoreService.findScoreAll();
            if (scoreAll.size() > 0) {
                for (Score score : scoreAll) {
                    User user1 = userService.findOne(score.getScorredcode());
                    User user2 = userService.findOne(score.getScorringcode());
                    if (user1 == null || user2 == null) {
                        scoreService.deleteByPrimaryKey(score.getId());
                    }
                }
                map.put("msg", "成功");
            }
        } catch (Exception E) {
            map.put("msg", "失败");
        }
        return map;
    }
}
