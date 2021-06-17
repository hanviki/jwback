package com.welb.organization_check.controller;

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
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ResultDetailController
 * @projectName xh-360appraisal-interface
 * @description: 综合结果详情报告控制类
 * @date 2019/6/2612:15
 */
@RestController
@RequestMapping("/resultdetail")
public class ResultDetailController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IScoreDetailService scoreDetailService;
    @Resource
    IResultDetailService resultDetailService;
    @Resource
    IDutyService dutyService;
    @Resource
    IUserService userService;
    @Resource
    IResultReportService reportService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IManualSetTimeService setTimeService;

    /**
     * 查询综合结果报告详情信息
     *
     * @param usercode
     * @param id
     * @return
     */
    @RequestMapping(value = "/getSingleTotalScore", produces = "application/json;charset=utf-8")
    public Object getSingleTotalScore(HttpServletRequest req, String usercode, Integer id, String year, String month) throws ParseException {
        ModelMap map = new ModelMap();
        //登录用户的编号
        String usercode1 = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode1 != null) {
            ResultDetail detail = new ResultDetail();
            //获取综合结果报告数据
            ResultReport report = reportService.selectByPrimaryKey(id);
            //获取综合结果详情数据
            List<ResultDetail> resultDetails = resultDetailService.selectResultDetailByReportCode(id);
            User user = userService.findUserByUserCode(usercode);
            if (user.getStationcode() != null || !user.getStationcode().equals("")) {
                try {
                    //获取指标类型为基础评分类型的相关数据
                    List<Duty> jichu = dutyService.queryJiChu(user.getStationcode());
                    //获取指标类型为关键评分类型的相关数据
                    List<Duty> yiBan = dutyService.queryYiBan(user.getStationcode());
                    if (year.equals("") && month.equals("")) {
                        String sysYear = CalendarUtil.getYear();
                        String sysMonth = CalendarUtil.getMonth();
                        String quarter = CalendarUtil.getQuarter(sysMonth);
                        int count = Integer.parseInt(quarter.trim()) - 1;
                        //获取当前系统时间
                        String sysTime = DateUtil.getTime();
                        if (state.equals("1")) {
                            //手动考核-查看所有季节总结
                            manualGetSingleTotalScore(usercode, id, year, detail, report, resultDetails, user, jichu, yiBan, sysYear, quarter, count, sysTime);

                        } else {
                            //自动考核-查看所有季节总结
                            automaticGetSingleTotalScore(usercode, id, detail, report, resultDetails, user, jichu, yiBan, sysYear, count);
                        }

                    } else {
                        getDetailInfo(usercode, id, year, month, detail, report, resultDetails, user, jichu, yiBan);
                    }

                    resultDetails = resultDetailService.selectResultDetailByReportCode(id);
                    for (int i = 0; i < resultDetails.size(); i++) {
                        List<UserRoleKey> roles = userRoleService.selectUserRoleByUserCode(usercode1);
                        if (roles.size() > 0) {
                            for (UserRoleKey role : roles) {
                                if (role.getRolecode().equals("300")) {
                                    resultDetails.get(i).setAscore(0.0);
                                    resultDetails.get(i).setBscore(0.0);
                                    resultDetails.get(i).setCscore(0.0);
                                    resultDetails.get(i).setDscore(0.0);
                                }
                            }
                        }

                    }
                    map.put("msg", "查询成功");
                    map.put("data", resultDetails);
                    map.put("code", 0);
                } catch (Exception e) {
                    log.error(LogUtil.getTrace(e));
                    map.put("msg", "查询失败");
                    map.put("code", 1);
                }
            } else {
                map.put("msg", "暂无数据");
                map.put("code", 0);
            }

        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }


        return map;
    }

    private void manualGetSingleTotalScore(String usercode, Integer id, String year, ResultDetail detail, ResultReport report, List<ResultDetail> resultDetails, User user, List<Duty> jichu, List<Duty> yiBan, String sysYear, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(sysYear, quarter);
        if (setTime != null) {

            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                year=sysYear;
                getDetailInfo(usercode, id, year, month, detail, report, resultDetails, user, jichu, yiBan);

            }else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticGetSingleTotalScore(usercode, id, detail, report, resultDetails, user, jichu, yiBan, sysYear, count);
            }
        }else {
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "4";
            } else {
                month = String.valueOf(count);
                year=sysYear;
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                getDetailInfo(usercode, id, year, month, detail, report, resultDetails, user, jichu, yiBan);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth==0){
                    lastMonth=4;
                    int lastYear = Integer.parseInt(year)-1;
                    year=String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                getDetailInfo(usercode, id, year, month, detail, report, resultDetails, user, jichu, yiBan);
            }
        }
    }

    private void getDetailInfo(String usercode, Integer id, String year, String month, ResultDetail detail, ResultReport report, List<ResultDetail> resultDetails, User user, List<Duty> jichu, List<Duty> yiBan) {
        String mserialno = year + "-" + month + "-" + usercode;
        if (report.getResultreportcode().equals("0")) {//基础评分详情
            getResultDetailInfo(id, detail, report, resultDetails, user, jichu, mserialno);
        } else {//关键评分详情
            getResultDetailInfo(id, detail, report, resultDetails, user, yiBan, mserialno);
        }
    }

    private void automaticGetSingleTotalScore(String usercode, Integer id, ResultDetail detail, ResultReport
            report, List<ResultDetail> resultDetails, User user, List<Duty> jichu, List<Duty> yiBan, String
                                                      sysYear, int count) {
        String year;
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(sysYear) - 1;
            year = String.valueOf(lastyear);
            month = "4";
        } else {
            year = sysYear;
            month = String.valueOf(count);
        }
        getDetailInfo(usercode, id, year, month, detail, report, resultDetails, user, jichu, yiBan);
    }

    private void getResultDetailInfo(Integer id, ResultDetail detail, ResultReport
            report, List<ResultDetail> resultDetails, User user, List<Duty> yiBan, String mserialno) {
        for (int i = 0; i < yiBan.size(); i++) {
            //获取评分类型为A的每道题的总分
            Double AScore = scoreDetailService.getSingleTotalScoreByType(mserialno, "A", report.getResultreportcode(), i + 1);
            //获取评分类型为A的每道题的的打分人数
            int ACount = scoreDetailService.getCountByType(mserialno, "A", report.getResultreportcode(), i + 1);
            Double AScore1;
            if (AScore == null) {
                AScore1 = 0.0;
            } else {
                AScore1 = AScore / ACount * user.getAratio() / 100;
            }
            //获取评分类型为B的每道题的总分
            Double BScore = scoreDetailService.getSingleTotalScoreByType(mserialno, "B", report.getResultreportcode(), i + 1);
            //获取评分类型为B的每道题的的打分人数
            int BCount = scoreDetailService.getCountByType(mserialno, "B", report.getResultreportcode(), i + 1);
            Double BScore1;
            if (BScore == null) {
                BScore1 = 0.0;
            } else {
                BScore1 = BScore / BCount * user.getBratio() / 100;
            }
            //获取评分类型为C的每道题的总分
            Double CScore = scoreDetailService.getSingleTotalScoreByType(mserialno, "C", report.getResultreportcode(), i + 1);
            //获取评分类型为C的每道题的的打分人数
            int CCount = scoreDetailService.getCountByType(mserialno, "C", report.getResultreportcode(), i + 1);
            Double CScore1;
            if (CScore == null) {
                CScore1 = 0.0;
            } else {
                CScore1 = CScore / CCount * user.getCratio() / 100;
            }
            //获取评分类型为A的每道题的总分
            Double DScore = scoreDetailService.getSingleTotalScoreByType(mserialno, "D", report.getResultreportcode(), i + 1);
            //获取评分类型为A的每道题的的打分人数
            int DCount = scoreDetailService.getCountByType(mserialno, "D", report.getResultreportcode(), i + 1);
            Double DScore1;
            if (DScore == null) {
                DScore1 = 0.0;
            } else {
                DScore1 = DScore / DCount * user.getDratio() / 100;
            }
            Double score = AScore1 + BScore1 + CScore1 + DScore1;
            String format = new DecimalFormat().format(score);
            detail.setScore(Double.parseDouble(format));
            detail.setAscore(AScore1);
            detail.setBscore(BScore1);
            detail.setCscore(CScore1);
            detail.setDscore(DScore1);
            Double avgscore = report.getAvgscore() * 0.2;
            detail.setAvgscore(avgscore);
            detail.setResultreportcode(id);
            detail.setDutyname(yiBan.get(i).getDutyname());
            detail.setOrderid(i + 1);
            //如果综合结果详情数据为空，则添加数据
            if (resultDetails.size() == 0) {
                resultDetailService.insertSelective(detail);
            } else {
                //不为空，则修改数据
                detail.setId(resultDetails.get(i).getId());
                resultDetailService.updateByPrimaryKeySelective(detail);
            }

        }
    }


    @RequestMapping(value = "/getScorringAndScore", produces = "application/json;charset=utf-8")
    public Object getScorringAndScore(HttpServletRequest req, Integer id, String usercode, String type, String
            year, String month) {
        ModelMap map = new ModelMap();
        String usercode1 = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode1 != null) {
            try {
                ResultDetail detail = resultDetailService.selectByPrimaryKey(id);
                //获取resultreport数据
                ResultReport resultReport = reportService.selectByPrimaryKey(detail.getResultreportcode());
                if (year.equals("") && month.equals("")) {
                    year = CalendarUtil.getYear();
                    month = CalendarUtil.getMonth();
                    String quarter = CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(quarter.trim()) - 1;
                    //获取当前系统时间
                    String sysTime = DateUtil.getTime();
                    if (state.equals("1")) {
                        //手动考核-查看所有季节总结
                        manualGetScorringAndScore(usercode, type, year, map, detail, resultReport, quarter, count, sysTime);
                    } else {
                        //自动考核-查看所有季节总结
                        automaticGetScorringAndScore(usercode, type, year, map, detail, resultReport, count);
                    }

                } else {
                    getFlows(usercode, type, year, month, map, detail, resultReport);
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

    private void manualGetScorringAndScore(String usercode, String type, String year, ModelMap map, ResultDetail detail, ResultReport resultReport, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                getFlows(usercode, type, year, month, map, detail, resultReport);
            }else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticGetScorringAndScore(usercode, type, year, map, detail, resultReport, count);
            }
        }else {
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "4";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                getFlows(usercode, type, year, month, map, detail, resultReport);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth==0){
                    lastMonth=4;
                    int lastYear = Integer.parseInt(year)-1;
                    year=String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                getFlows(usercode, type, year, month, map, detail, resultReport);
            }
        }
    }

    private void getFlows(String usercode, String type, String year, String month, ModelMap map, ResultDetail detail, ResultReport resultReport) {
        String mserialno = year + "-" + month + "-" + usercode;
        List<ScoreFlow> flows = flowService.getSingleTotalScoreAll(mserialno, type, resultReport.getResultreportcode(), detail.getOrderid());
        List<ScoreDetail> singleTotalScore = scoreDetailService.getSingleTotalScore(mserialno, type, resultReport.getResultreportcode(), detail.getOrderid());
        getSingScore(flows, singleTotalScore);
        map.put("msg", "查询成功");
        map.put("data", flows);
        map.put("code", 0);
    }

    private void automaticGetScorringAndScore(String usercode, String type, String year, ModelMap map, ResultDetail detail, ResultReport resultReport, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "4";
        } else {
            month = String.valueOf(count);
        }
        getFlows(usercode, type, year, month, map, detail, resultReport);
    }

    private void getSingScore(List<ScoreFlow> flows, List<ScoreDetail> singleTotalScore) {
        if (flows.size() > 0) {
            for (int i = 0; i < flows.size(); i++) {
                //获取题目
                Duty duty = dutyService.selectByPrimaryKey(singleTotalScore.get(i).getDSerialNo());
                flows.get(i).setDutyname(duty.getDutyname());
                //获取评分人姓名、部门、岗位、每道题的分数
                User user = userService.findUserByUserCode(flows.get(i).getScorringcode());
                flows.get(i).setScorringname(user.getUsername());
                Station station = stationService.selectByStationCode(user.getStationcode());
                if (station != null) {
                    flows.get(i).setStationname(station.getStationname());
                    Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
                    if (department != null) {
                        flows.get(i).setDeparmentname(department.getDepartmentname());
                    } else {
                        flows.get(i).setDeparmentname("无部门");
                    }
                } else {
                    flows.get(i).setStationname("无岗位");
                }

                flows.get(i).setSinglescore(singleTotalScore.get(i).getScore());
            }
        }
    }


}
