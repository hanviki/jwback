package com.welb.organization_check.controller;

import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserSummaryDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.welb.sysBase.util.Constant.MEDICAL_ETHICS_USER_ROLE;

/**
 * @author luoyaozu
 * @title: SendMessageController
 * @projectName xh-360appraisal-interface
 * @description: 移动端接口
 * @date 2019/7/114:56
 */
@RestController
@RequestMapping("/mobile")
public class MobileController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IStationService stationService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IDutyService dutyService;
    @Resource
    IScoreDetailService detailService;
    @Resource
    IUserSummaryDtoService dtoService;
    @Resource
    IUserService userService;
    @Resource
    IEvaluationReportService evaluationReportService;
    @Resource
    IResultReportService resultReportService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IResultDetailService resultDetailService;
    @Resource
    IScoreService scoreService;
    @Resource
    IUserSummaryDtoService summaryDtoService;
    @Resource
    IRoleService roleService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IAssessmentStateService stateService;
    @Resource
    IScoreHistoryService historyService;
    @Resource
    IUserDtoService userDtoService;
    @Autowired
    MedicalEthicsUserService medicalEthicsUserService;

    /**
     * 移动端查询个人考核详情
     *
     * @return
     */
    @RequestMapping(value = "/getDetail", produces = "application/json;charset=utf-8")
    public Object getDetail(HttpServletRequest req, UserSummaryDto dto, String scorringcode, String isvalidation) {//isvalidation 0:需要验证  1:不需要验证
        ModelMap map = new ModelMap();
        Map<String, Object> data = new LinkedHashMap<>();
        if (isvalidation.equals("1")) {
            try {
                if (dto.getYear() != null && dto.getMonth() != null) {
                    dto = dtoService.selectUserSummaryByLike(dto);
                    if (dto != null) {
                        //查找岗位
                        dto.setMonth(dto.getMonth());
                        Station station = stationService.selectByStationCode(dto.getStationcode());
                        //获取评分人给被评分人打分的情况
                        List<ScoreFlow> flow = flowService.selectByScoreFlow(dto.getSerialno(), scorringcode);
                        getFlow(map, data, dto, station, flow);
                    } else {
                        map.put("msg", "数据为空");
                        map.put("code", 0);
                    }
                } else {
                    String state = (String) req.getSession().getAttribute("state");
//                    当前年份
                    String year = CalendarUtil.getYear();
//                    当前月份h
                    String month = CalendarUtil.getMonth();
//                    当前季度
                 //   String quarter = CalendarUtil.getQuarter(month);
                    //当前上一季度
                    int count = Integer.parseInt(month.trim()) - 1;
                    //获取当前系统时间
                    String sysTime = DateUtil.getTime();
                    if (state.equals("1")) {
                        //手动考核-查看所有季节总结
                        manualGetDetail(dto, map, data, year, month, count, sysTime);

                    } else {
                        //自动考核-查看所有季节总结
                        automaticGetDetail(dto, map, data, year, count);
                    }

                }
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询个人考核详情失败");
                map.put("code", 1);
            }
        }
        return map;
    }

    private void manualGetDetail(UserSummaryDto dto, ModelMap map, Map<String, Object> data, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                getDto(dto, map, data, year, month);
            } else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticGetDetail(dto, map, data, year, count);
            }
        } else {
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                getDto(dto, map, data, year, month);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                getDto(dto, map, data, year, month);
            }
        }
    }

    private void getDto(UserSummaryDto dto, ModelMap map, Map<String, Object> data, String year, String month) {
        dto.setYear(year);
        dto.setMonth(month);
        dto = dtoService.selectUserSummaryByLike(dto);
        //查找岗位
        Station station = stationService.selectByStationCode(dto.getStationcode());
        //获取评分人给被评分人打分的情况
        List<ScoreFlow> flow = flowService.selectByScoreFlow(dto.getSerialno(), dto.getScorringcode());
        getFlow(map, data, dto, station, flow);
    }

    private void automaticGetDetail(UserSummaryDto dto, ModelMap map, Map<String, Object> data, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";

        } else {
            month = String.valueOf(count);
        }
        dto.setYear(year);
        dto.setMonth(month);
        getDto(dto, map, data, year, month);
    }


    private void getFlow(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow) {
        if (scoreFlow.size() > 0) {
            for (ScoreFlow flow : scoreFlow) {

                data.put("total", flow.getScore() + "分");
                //获取基础量化指标相关信息
                List<Duty> dutyJichu = dutyService.queryJiChu(dto.getStationcode());
                getDutyInfo(data, flow, dutyJichu);
                //获取关键量化指标的相关信息
                List<Duty> dutyYiban = dutyService.queryYiBan(dto.getStationcode());
                getDutyInfo(data, flow, dutyYiban);

                //判断是否可编辑
                if (dto.getState().equals("7")) {
                    dto.setIsedit("1");
                } else {
                    dto.setIsedit("0");
                }
                data.put("detail", dto);
                data.put("stations", station);
                data.put("dutyJichu", dutyJichu);
                data.put("dutyYiban", dutyYiban);
                map.put("msg", "查询个人考核详情成功");
                map.put("data", data);
                map.put("code", 0);
            }
        } else {
            data.put("total", "");
            List<Duty> dutyJichu = dutyService.queryJiChu(dto.getStationcode());
            for (Duty duty : dutyJichu) {
                duty.setScore("");
            }
            //获取关键量化指标的相关信息
            List<Duty> dutyYiban = dutyService.queryYiBan(dto.getStationcode());
            for (Duty duty : dutyYiban) {
                duty.setScore("");
            }
            if (dto.getState().equals("7")) {
                dto.setIsedit("1");
            } else {
                dto.setIsedit("0");
            }
            //获取基础量化指标相关信息
            data.put("detail", dto);
            data.put("stations", station);
            data.put("dutyJichu", dutyJichu);
            data.put("dutyYiban", dutyYiban);
            map.put("msg", "查询个人考核详情成功");
            map.put("data", data);
            map.put("code", 0);
        }
    }

    private void getDutyInfo(Map<String, Object> data, ScoreFlow flow, List<Duty> dutyJichu) {
        for (Duty duty : dutyJichu) {
            ScoreDetail detail = detailService.selectDetailBySerialNo(duty.getDutycode(), flow.getSerialno());
            if (data != null) {
                duty.setScore(detail.getScore());
            } else {
                duty.setScore("");
            }

        }
    }


    /**
     * 发送个人评估报告短信接口
     *
     * @return
     */
    @RequestMapping(value = "/evaluationReport", produces = "application/json;charset=utf-8")
    public Object sendEvaluationReport(HttpServletRequest req, String usercode, String isvalidation) {
        ModelMap map = new ModelMap();
        List<Role> roles = roleService.selectRoleListByUserCode(usercode);
        if (roles.size() > 0) {
            for (Role role : roles) {
                if (role.getRolecode().equals("50") || role.getRolecode().equals("100") ||
                        role.getRolecode().equals("150") || role.getRolecode().equals("200") || role.getRolecode().equals("300")) {
                    String state = (String) req.getSession().getAttribute("state");
                    Map<String, Object> data = new LinkedHashMap<>();
                    if (isvalidation.equals("1")) {
                        EvaluationReport report = new EvaluationReport();
                        ResultReport r1 = new ResultReport();
                        ResultReport r2 = new ResultReport();
                        User user = userService.findUserByUserCode(usercode);
                        //保留两位小数
                        DecimalFormat df = new DecimalFormat("0.00");
//            获取用户信息
                        getUserInfo(report, usercode);
                        try {
//            年份
                            String year = CalendarUtil.getYear();
//            月份
                            String month = CalendarUtil.getMonth();
//            季度
                          //  String quarter = CalendarUtil.getQuarter(month);
                            int count = Integer.parseInt(month) - 1;
                            //获取当前系统时间
                            String sysTime = DateUtil.getTime();
                            if (state.equals("1")) {
                                //手动考核-查看所有季节总结
                                manualSendEvaluationReport(usercode, report, r1, r2, user, df, year, month, count, sysTime);

                            } else {
                                //自动考核-查看所有季节总结
                                automaticSendEvaluationReport(usercode, report, r1, r2, user, df, year, count);
                            }

                            EvaluationReport evaluationReport = evaluationReportService.selectReportByUserCode(report);
                            if (evaluationReport != null) {
                                evaluationReport.setTotalscore(report.getTotalscore());
                                evaluationReport.setKeyscore(report.getKeyscore());
                                evaluationReport.setBasicscore(report.getBasicscore());
                                evaluationReport.setMincomparelast(report.getMincomparelast());
                                evaluationReport.setMaxcomparelast(report.getMaxcomparelast());
                                evaluationReport.setTotalcomparelast(report.getTotalcomparelast());
                                evaluationReport.setMaxscore(report.getMaxscore());
                                evaluationReport.setMinscore(report.getMinscore());
                                evaluationReport.setScoredifference(report.getScoredifference());
                                evaluationReportService.updateByPrimaryKeySelective(evaluationReport);
                                int reportCount = evaluationReportService.selectMaxAndMinReportCount(report.getYear(), report.getMonth());
                                Double sumOfMaxScore = evaluationReportService.selectSumOfMaxScore(report.getYear(), report.getMonth());
                                Double sumOfMinScore = evaluationReportService.selectSumOfMinScore(report.getYear(), report.getMonth());
                                Double avgMaxScore = sumOfMaxScore / reportCount;
                                Double avgMinScore = sumOfMinScore / reportCount;
                                Double maxCompareMark = (report.getMaxscore() - avgMaxScore) / avgMaxScore;
                                Double minCompareMark = (report.getMinscore() - avgMinScore) / avgMinScore;
                                evaluationReport.setMaxcomparemark(maxCompareMark);
                                evaluationReport.setMincomparemark(minCompareMark);
                                evaluationReport.setMincomparelast(report.getMincomparelast());
                                evaluationReport.setMaxcomparelast(report.getMaxcomparelast());
                                //            第七步  计算个人得分较整体平均分差距半分比
                                double totalCompareMark = (report.getTotalscore() - report.getAvgscore()) / report.getAvgscore();
                                evaluationReport.setTotalcomparemark(totalCompareMark);
                                evaluationReportService.updateByPrimaryKeySelective(evaluationReport);
                                ResultReport resultReport1 = resultReportService.selectResultReportByCode("0", evaluationReport.getId());
                                getResultReport(evaluationReport, r1, resultReport1);
                                ResultReport resultReport2 = resultReportService.selectResultReportByCode("1", evaluationReport.getId());
                                getResultReport(evaluationReport, r2, resultReport2);
                                List<ResultReport> reports = resultReportService.selectResultReportByEvaluationCode(evaluationReport.getId());
                                EvaluationReport newReprot = evaluationReportService.selectByPrimaryKey(evaluationReport.getId());
                                newReprot.setUsername(report.getUsername());
                                newReprot.setStationname(report.getStationname());
                                newReprot.setDepartmentname(report.getDepartmentname());
                                if (reports.size() > 0) {
                                    data.put("reports", reports);
                                } else {
                                    map.put("msg", "数据为空");
                                    map.put("code", 2);
                                }
                                data.put("evaluationReport", newReprot);
                                map.put("msg", "查询测评报告成功");
                                map.put("data", data);
                                map.put("code", 0);

                            } else {
                                map.put("msg", "暂未生成个人估报告");
                                map.put("code", 2);
                            }
                        } catch (Exception e) {
                            log.error(LogUtil.getTrace(e));
                            map.put("msg", "查询测评报告失败");
                            map.put("code", 1);
                        }
                    }
                    break;

                } else {
                    map.put("msg", "该用户不是组织部成员或该用户不存在,没有权限进入");
                    map.put("code", 1);
                }
            }
        } else {
            map.put("msg", "该用户没有角色或该用户不存在,请联系管理员");
            map.put("code", 1);
        }
        return map;
    }

    private void manualSendEvaluationReport(String usercode, EvaluationReport report, ResultReport r1, ResultReport r2, User user, DecimalFormat df, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                getEvaluationReports(usercode, report, r1, r2, user, df, year, month);
            } else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticSendEvaluationReport(usercode, report, r1, r2, user, df, year, count);
            }
        } else {
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                getEvaluationReports(usercode, report, r1, r2, user, df, year, month);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                getEvaluationReports(usercode, report, r1, r2, user, df, year, month);
            }
        }
    }

    private void getEvaluationReports(String usercode, EvaluationReport report, ResultReport r1, ResultReport r2, User user, DecimalFormat df, String year, String month) {
        report.setYear(year);
        report.setMonth(month);
        report.setUsercode(usercode);
        EvaluationReport evaluation = evaluationReportService.selectReportByUserCode(report);
        if (evaluation != null) {
            report.setAvgscore(evaluation.getAvgscore());
            report.setTotalscore(evaluation.getTotalscore());
        } else {
            report.setAvgscore(0.0);
            report.setTotalscore(0.0);
        }
        List<ScoreFlow> scoreFlows = flowService.selectScoreAllByScoredCode(report.getUsercode(), year, month);
        getScoreInfo(report, df, user, year, month, scoreFlows, r1, r2);
    }

    private void automaticSendEvaluationReport(String usercode, EvaluationReport report, ResultReport r1, ResultReport r2, User user, DecimalFormat df, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            report.setYear(year);
            month = String.valueOf(count);
        }
        getEvaluationReports(usercode, report, r1, r2, user, df, year, month);
    }

    private void getResultReport(EvaluationReport evaluationReport, ResultReport r, ResultReport resultReport) {
        if (resultReport == null) {
            r.setEvaluationreportcode(evaluationReport.getId());
            resultReportService.insertSelective(r);
        } else {
            r.setId(resultReport.getId());
            resultReportService.updateByPrimaryKeySelective(r);
        }
    }

    private void getScoreInfo(EvaluationReport report, DecimalFormat df, User user, String year, String month, List<ScoreFlow> scoreFlows, ResultReport r1, ResultReport r2) {
        if (scoreFlows.size() > 0) {
            String mserialno = scoreFlows.get(0).getMserialno();
//            第二步  计算总分
            getReportTotalScore(report, df, user, mserialno, r1, r2);
//            第三步  获取最高分和最低分
            Double maxScore = flowService.selectMaxScoreByScoredCode(user.getUsercode(), year, month);
            Double minScore = flowService.selectMinScoreByScoredCode(user.getUsercode(), year, month);
            report.setMaxscore(maxScore);
            report.setMinscore(minScore);
//            第四步  计算分值差
            double scoreDifference = maxScore - minScore;
            report.setScoredifference(scoreDifference);
            EvaluationReport report1 = new EvaluationReport();
            report1.setUsercode(report.getUsercode());
            int count1 = Integer.parseInt(month) - 1;
            if (count1 == 0) {
                int lastyear1 = Integer.parseInt(year) - 1;
                report1.setYear(String.valueOf(lastyear1));
                report1.setMonth("12");
                EvaluationReport report2 = evaluationReportService.selectReportByUserCode(report1);
                getCompareLastInfo(report, maxScore, minScore, report2);
            } else {
                report1.setYear(year);
                report1.setMonth(String.valueOf(count1));
                EvaluationReport report2 = evaluationReportService.selectReportByUserCode(report1);
                getCompareLastInfo(report, maxScore, minScore, report2);
            }


        }
    }

    private void getReportTotalScore(EvaluationReport report, DecimalFormat df, User user, String mserialno, ResultReport r1, ResultReport r2) {
        //      获取各类评分人的数量
        int Acount = flowService.getScoreByTypeCount(mserialno, "A");
        int Bcount = flowService.getScoreByTypeCount(mserialno, "B");
        int Ccount = flowService.getScoreByTypeCount(mserialno, "C");
        int Dcount = flowService.getScoreByTypeCount(mserialno, "D");
//     计算基础得分
        getBasicTotalScore(report, user, mserialno, Acount, Bcount, Ccount, Dcount, df, r1);

//     计算关键得分
        getTotalKeyScore(report, user, mserialno, Acount, Bcount, Ccount, Dcount, df, r2);
    }

    private void getTotalKeyScore(EvaluationReport report, User user, String mserialno, int acount, int bcount, int ccount, int dcount, DecimalFormat df, ResultReport r2) {
        Double keyAScore = detailService.getTotalScoreByType(mserialno, "A", "1");
        Double keyAScore1;
        Double totalRatio = 0.0;
        if (keyAScore == null) {
            keyAScore1 = 0.0;
        } else {
            keyAScore1 = keyAScore / acount;
            totalRatio += user.getAratio();
        }
        Double keyBScore = detailService.getTotalScoreByType(mserialno, "B", "1");
        Double keyBScore1;
        if (keyBScore == null) {
            keyBScore1 = 0.0;
        } else {
            keyBScore1 = keyBScore / bcount;
            totalRatio += user.getBratio();
        }
        Double keyCScore = detailService.getTotalScoreByType(mserialno, "C", "1");
        Double keyCScore1;
        if (keyCScore == null) {
            keyCScore1 = 0.0;
        } else {
            keyCScore1 = keyCScore / ccount;
            totalRatio += user.getCratio();
        }
        Double keyDScore = detailService.getTotalScoreByType(mserialno, "D", "1");
        Double keyDScore1;
        if (keyDScore == null) {
            keyDScore1 = 0.0;
        } else {
            keyDScore1 = keyDScore / dcount;
            totalRatio += user.getDratio();
        }
        Double totalKeyScore = report.getTotalscore() - report.getBasicscore();
        report.setKeyscore(Double.parseDouble(df.format(totalKeyScore)));
        r2.setResultreportcode("1");
        r2.setResultreportname("关键评分");
        r2.setAscore(keyAScore1);
        r2.setBscore(keyBScore1);
        r2.setCscore(keyCScore1);
        r2.setDscore(keyDScore1);
        r2.setScore(report.getKeyscore());
        r2.setAvgscore(report.getAvgscore() * 0.8);
    }

    private void getBasicTotalScore(EvaluationReport report, User user, String mserialno, int acount, int bcount, int ccount, int dcount, DecimalFormat df, ResultReport r1) {
        Double basicAScore = detailService.getTotalScoreByType(mserialno, "A", "0");
        Double basicAScore1;
        Double totalRatio = 0.0;
        if (basicAScore == null) {
            basicAScore1 = 0.0;

        } else {
            basicAScore1 = basicAScore / acount;
            totalRatio += user.getAratio();
        }
        Double basicBScore = detailService.getTotalScoreByType(mserialno, "B", "0");
        Double basicBScore1;
        if (basicBScore == null) {
            basicBScore1 = 0.0;
        } else {
            basicBScore1 = basicBScore / bcount;
            totalRatio += user.getBratio();
        }
        Double basicCScore = detailService.getTotalScoreByType(mserialno, "C", "0");
        Double basicCScore1;
        if (basicCScore == null) {
            basicCScore1 = 0.0;
        } else {
            basicCScore1 = basicCScore / ccount;
            totalRatio += user.getCratio();

        }
        Double basicDScore = detailService.getTotalScoreByType(mserialno, "D", "0");
        Double basicDScore1;
        if (basicDScore == null) {
            basicDScore1 = 0.0;
        } else {
            basicDScore1 = basicDScore / dcount;
            totalRatio += user.getDratio();
        }
        Double totalBasicScore = basicAScore1 * user.getAratio() + basicBScore1 * user.getBratio() + basicCScore1 * user.getCratio() + basicDScore1 * user.getDratio();
        String format = df.format(totalBasicScore / totalRatio);
        report.setBasicscore(Double.parseDouble(format));
        r1.setResultreportname("基础评分");
        r1.setResultreportcode("0");
        r1.setAscore(basicAScore1);
        r1.setBscore(basicBScore1);
        r1.setCscore(basicCScore1);
        r1.setDscore(basicDScore1);
        r1.setScore(report.getBasicscore());
        r1.setAvgscore(report.getAvgscore() * 0.2);
    }

    private void getCompareLastInfo(EvaluationReport report, Double maxScore, Double minScore, EvaluationReport report2) {
        if (report2 == null) {
            report.setMaxcomparelast(0.0);
            report.setMincomparelast(0.0);
            report.setTotalcomparelast(0.0);
        } else {
            double maxCompareLast = maxScore - report2.getMaxscore();
            double minCompareLast = minScore - report2.getMinscore();
            double totalCompareLast = (report.getTotalscore() - report2.getTotalscore()) / report2.getTotalscore();
            report.setMaxcomparelast(maxCompareLast);
            report.setMincomparelast(minCompareLast);
            report.setTotalcomparelast(totalCompareLast);
        }
    }

    private void getUserInfo(EvaluationReport report, String usercode) {
        User user = userService.findUserByUserCode(usercode);
        report.setUsername(user.getUsername());
//        获取岗位和部门名称
        Station station = stationService.selectByStationCode(user.getStationcode());
        EvaluationReportController.getStationInfo(report, station, departmentService);
    }


    /**
     * 查询综合结果报告详情信息
     *
     * @param usercode
     * @param id
     * @return
     */
    @RequestMapping(value = "/getSingleTotalScore", produces = "application/json;charset=utf-8")
    public Object getSingleTotalScore(HttpServletRequest req, String usercode, Integer id, String isvalidation) {
        ModelMap map = new ModelMap();
        String state = (String) req.getSession().getAttribute("state");
        if (isvalidation.equals("1")) {
            ResultDetail detail = new ResultDetail();
            //获取综合结果报告数据
            ResultReport report = resultReportService.selectByPrimaryKey(id);
            //获取综合结果详情数据
            List<ResultDetail> resultDetails = resultDetailService.selectResultDetailByReportCode(id);
            User user = userService.findUserByUserCode(usercode);
            if (user.getStationcode() != null || !user.getStationcode().equals("")) {
                try {
                    //获取指标类型为基础评分类型的相关数据
                    List<Duty> jichu = dutyService.queryJiChu(user.getStationcode());
                    //获取指标类型为关键评分类型的相关数据
                    List<Duty> yiBan = dutyService.queryYiBan(user.getStationcode());
                    String year = CalendarUtil.getYear();
                    String month = CalendarUtil.getMonth();
                  //  String quarter = CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(month.trim()) - 1;
                    //获取当前系统时间
                    String sysTime = DateUtil.getTime();
                    if (state.equals("1")) {
                        //手动考核-查看所有季节总结
                        manualGetSerialNo(usercode, id, detail, report, resultDetails, user, jichu, yiBan, year, month, count, sysTime);
                    } else {
                        //自动考核-查看所有季节总结
                        automaticGetSerialNo(usercode, id, detail, report, resultDetails, user, jichu, yiBan, year, count);
                    }

                    resultDetails = resultDetailService.selectResultDetailByReportCode(id);
                    for (int i = 0; i < resultDetails.size(); i++) {
                        List<UserRoleKey> roles = userRoleService.selectUserRoleByUserCode(usercode);
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
                map.put("msg", "数据为空");
                map.put("code", 2);
            }
        }
        return map;
    }

    private void manualGetSerialNo(String usercode, Integer id, ResultDetail detail, ResultReport report, List<ResultDetail> resultDetails, User user, List<Duty> jichu, List<Duty> yiBan, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                getSerialNo(usercode, id, detail, report, resultDetails, user, jichu, yiBan, year, month);
            } else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticGetSerialNo(usercode, id, detail, report, resultDetails, user, jichu, yiBan, year, count);
            }
        } else {

            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                getSerialNo(usercode, id, detail, report, resultDetails, user, jichu, yiBan, year, month);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                getSerialNo(usercode, id, detail, report, resultDetails, user, jichu, yiBan, year, month);
            }

        }
    }

    private void getSerialNo(String usercode, Integer id, ResultDetail detail, ResultReport report, List<ResultDetail> resultDetails, User user, List<Duty> jichu, List<Duty> yiBan, String year, String month) {
        String mserialno = year + "-" + month + "-" + usercode;
        if (report.getResultreportcode().equals("0")) {//基础评分详情
            getResultDetailInfo(id, detail, report, resultDetails, user, jichu, mserialno);
        } else {//关键评分详情
            getResultDetailInfo(id, detail, report, resultDetails, user, yiBan, mserialno);
        }
    }

    private void automaticGetSerialNo(String usercode, Integer id, ResultDetail detail, ResultReport report, List<ResultDetail> resultDetails, User user, List<Duty> jichu, List<Duty> yiBan, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(count);
        }
        getSerialNo(usercode, id, detail, report, resultDetails, user, jichu, yiBan, year, month);
    }

    private void getResultDetailInfo(Integer id, ResultDetail detail, ResultReport report, List<ResultDetail> resultDetails, User user, List<Duty> yiBan, String mserialno) {
        for (int i = 0; i < yiBan.size(); i++) {
            //获取评分类型为A的每道题的总分
            Double AScore = detailService.getSingleTotalScoreByType(mserialno, "A", report.getResultreportcode(), i + 1);
            //获取评分类型为A的每道题的的打分人数
            int ACount = detailService.getCountByType(mserialno, "A", report.getResultreportcode(), i + 1);
            Double AScore1;
            if (AScore == null) {
                AScore1 = 0.0;
            } else {
                AScore1 = AScore / ACount * user.getAratio() / 100;
            }
            //获取评分类型为B的每道题的总分
            Double BScore = detailService.getSingleTotalScoreByType(mserialno, "B", report.getResultreportcode(), i + 1);
            //获取评分类型为B的每道题的的打分人数
            int BCount = detailService.getCountByType(mserialno, "B", report.getResultreportcode(), i + 1);
            Double BScore1;
            if (BScore == null) {
                BScore1 = 0.0;
            } else {
                BScore1 = BScore / BCount * user.getBratio() / 100;
            }
            //获取评分类型为C的每道题的总分
            Double CScore = detailService.getSingleTotalScoreByType(mserialno, "C", report.getResultreportcode(), i + 1);
            //获取评分类型为C的每道题的的打分人数
            int CCount = detailService.getCountByType(mserialno, "C", report.getResultreportcode(), i + 1);
            Double CScore1;
            if (CScore == null) {
                CScore1 = 0.0;
            } else {
                CScore1 = CScore / CCount * user.getCratio() / 100;
            }
            //获取评分类型为A的每道题的总分
            Double DScore = detailService.getSingleTotalScoreByType(mserialno, "D", report.getResultreportcode(), i + 1);
            //获取评分类型为A的每道题的的打分人数
            int DCount = detailService.getCountByType(mserialno, "D", report.getResultreportcode(), i + 1);
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

    /**
     * 计算总分
     *
     * @param dto
     * @param dutyJiChu
     * @param dutyYiban
     * @return
     */
    @RequestMapping(value = "/getTotalScore", produces = "application/json;charset=utf-8")
    public Object getTotalScore(HttpServletRequest req, String scorringcode, UserSummaryDto dto, String dutyJiChu,
                                String dutyYiban, EvaluationReport report, String year, String month) {
        ModelMap map = new ModelMap();
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        String state = (String) req.getSession().getAttribute("state");
        try {
            ScoreFlow flow = new ScoreFlow();
            flow.setScorringcode(scorringcode);
            flow.setScoredcode(dto.getEmployeecode());
            flow.setMserialno(dto.getSerialno());
            flow.setScore(dto.getTotal());
            report.setTotalscore(dto.getTotal());
            new SimpleDateFormat("yyyy-MM-dd");
            flow.setScoredate(new Date());
            //通过评分人和被评分人code查找评分关系数据
            Score score = scoreService.selectTypeByCode(dto.getEmployeecode(), scorringcode);
            flow.setScoretype(score.getScoretype());
            User user = userService.findUserByUserCode(dto.getEmployeecode());
            if ("A".equals(flow.getScoretype())) {
                flow.setRatio(user.getAratio());
            } else if ("B".equals(flow.getScoretype())) {
                flow.setRatio(user.getBratio());
            } else if ("C".equals(flow.getScoretype())) {
                flow.setRatio(user.getCratio());
            } else if ("D".equals(flow.getScoretype())) {
                flow.setRatio(user.getDratio());
            }
            List<ScoreFlow> flow1 = flowService.selectByScoreFlow(dto.getSerialno(), scorringcode);
            if (flow1.size() > 0) {//不为空  则修改评分信息
                List<ScoreFlow> list = new ArrayList<>();
                for (ScoreFlow scoreFlow : flow1) {
                    scoreFlow.setScore(dto.getTotal());
                    list.add(scoreFlow);
                }
                if (list.size() > 0) {
                    flowService.batchUpdate(list);
                }
                for (ScoreFlow scoreFlow : list) {
                    getScore(dutyJiChu, dutyYiban, scoreFlow);
                }
            } else {
                flowService.insertSelective(flow);
                getScore(dutyJiChu, dutyYiban, flow);
            }
//          计算完总分之后  个人测评报告也相应的产生一条数据
            getEvaluationReportInfo(state, dto, report);
            /*========================被打分用户分数添加或更新  往历史评分数据表插数据==================================================*/
            addOrUpdateScoreHistory(dto, year, month, df, user);
            /*=========================打分用户的评分状态更新==========================================================================*/
            updateScoreStatus(scorringcode, year, month);
            map.put("msg", "操作成功");
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "操作失败");
            map.put("code", 1);
        }

        return map;
    }

    private void updateScoreStatus(String scorringcode, String year, String month) {
        ScoreHistory historyState = new ScoreHistory();
        historyState.setYear(year);
        historyState.setMonth(month);
        historyState.setUsercode(scorringcode);
        UserDto userDto = new UserDto();
        userDto.setYear(year);
        userDto.setMonth(month);
        userDto.setEmployeecode(scorringcode);
        int dtoTotalCount = userDtoService.getTotalCount(userDto);
        String mserialno = year + "-" + month;
        int flowTotalCount = flowService.getTotalCount(mserialno, scorringcode);
        if (flowTotalCount == 0) {
            historyState.setScorestatus("1");
        } else if (flowTotalCount < dtoTotalCount) {
            historyState.setScorestatus("2");
        } else {
            historyState.setScorestatus("3");
        }
        ScoreHistory history = historyService.selectOneByHistory(historyState);
        if (history == null) {//新增操作
            historyService.insertSelective(historyState);
        } else {//更新操作
            historyState.setId(history.getId());
            historyService.updateByPrimaryKeySelective(historyState);
        }
    }

    private ScoreHistory addOrUpdateScoreHistory(UserSummaryDto dto, String year, String month, DecimalFormat df, User user) {
        ScoreHistory history = new ScoreHistory();
        history.setYear(year);
        history.setMonth(month);
        history.setUsercode(dto.getEmployeecode());
        ScoreHistory scoreHistory = historyService.selectOneByHistory(history);
        getTypeUserScore(dto, df, user, history);
        if (scoreHistory == null) {//新增操作
            history.setScorestatus("1");
            historyService.insertSelective(history);
        } else {//更新操作
            history.setId(scoreHistory.getId());
            historyService.updateByPrimaryKeySelective(history);
        }
        return history;
    }

    private void getTypeUserScore(UserSummaryDto dto, DecimalFormat df, User user, ScoreHistory history) {
        Double totalRatio = 0.0;
        //A类评分
        Double AScore = flowService.getTypeAvgScore(dto.getSerialno(), "A");
        if (AScore == null) {
            AScore = 0.0;
            history.setAscore(AScore);
        } else {
            history.setAscore(Double.parseDouble(df.format(AScore)));
            totalRatio += user.getAratio();
        }
        //B类评分
        Double BScore = flowService.getTypeAvgScore(dto.getSerialno(), "B");
        if (BScore == null) {
            BScore = 0.0;
            history.setBscore(BScore);
        } else {
            history.setBscore(Double.parseDouble(df.format(BScore)));
            totalRatio += user.getBratio();
        }
        //C类评分
        Double CScore = flowService.getTypeAvgScore(dto.getSerialno(), "C");
        if (CScore == null) {
            CScore = 0.0;
            history.setCscore(CScore);
        } else {
            history.setCscore(Double.parseDouble(df.format(CScore)));
            totalRatio += user.getCratio();
        }
        //D类评分
        Double DScore = flowService.getTypeAvgScore(dto.getSerialno(), "D");
        if (DScore == null) {
            DScore = 0.0;
            history.setDscore(DScore);
        } else {
            history.setDscore(Double.parseDouble(df.format(DScore)));
            totalRatio += user.getDratio();
        }

        //获取总分  总分= A类总分 X A类评分人系数 +  B类总分 X B类评分人系数 + C类总分 X C类评分人系数 + D类总分 X D类评分人系数

        Double totalscore = history.getAscore() * user.getAratio() + history.getBscore() * user.getBratio() +
                history.getCscore() * user.getCratio() + history.getDscore() * user.getDratio();
        if (totalscore == 0.0 || totalscore == null) {
            history.setTotalscore(0.0);
        } else {
            history.setTotalscore(Double.parseDouble(df.format(totalscore / totalRatio)));
        }
    }



    private void getEvaluationReportInfo(String state, UserSummaryDto dto, EvaluationReport report) {
        EvaluationReport evaluationReport;
        report.setUsercode(dto.getEmployeecode());
//            年份
        String year = CalendarUtil.getYear();
//            月份
        String month = CalendarUtil.getMonth();
//            季度
      //  String quarter = CalendarUtil.getQuarter(month);
        int count = Integer.parseInt(month) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();
        if (state.equals("1")) {
            //手动考核-查看所有季节总结
            manualGetReport(report, year, month, count, sysTime);
        } else {
            //自动考核-查看所有季节总结
            automaticGetReport(report, year, count);
        }

        evaluationReport = evaluationReportService.selectReportByUserCode(report);
        if (evaluationReport == null) {
            evaluationReportService.insertSelective(report);
        } else {
            report.setId(evaluationReport.getId());
            evaluationReportService.updateByPrimaryKeySelective(report);
        }

    }

    private void manualGetReport(EvaluationReport report, String year, String quarter, int count, String sysTime) {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            try {
                if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                    //开始新的季度考核
                    month = quarter;
                    report.setYear(year);
                    report.setMonth(month);
                } else {
                    //未到达指定考核时间，仍展示上一季度数据
                    automaticGetReport(report, year, count);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (count == 0) {
                    int lastyear = Integer.parseInt(year.trim()) - 1;
                    year = String.valueOf(lastyear);
                    month = "12";
                } else {
                    month = String.valueOf(count);
                }
                ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
                if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                    report.setYear(year);
                    report.setMonth(month);
                } else {
                    int lastMonth = Integer.parseInt(month) - 1;
                    if (lastMonth == 0) {
                        lastMonth = 12;
                        int lastYear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastYear);
                    }
                    month = String.valueOf(lastMonth);
                    report.setYear(year);
                    report.setMonth(month);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void automaticGetReport(EvaluationReport report, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(count);
        }
        report.setYear(year);
        report.setMonth(month);
    }

    /**
     * 向scoreflow数据库表添加或修改数据
     *
     * @param dutyJiChu
     * @param dutyYiBan
     * @param flow1
     */

    private void getScore(String dutyJiChu, String dutyYiBan, ScoreFlow flow1) {
        ScoreDetail detail = new ScoreDetail();
        JSONArray array = JSONArray.fromObject(dutyJiChu);
        List jichu = JSONArray.toList(array, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, jichu);


        JSONArray array1 = JSONArray.fromObject(dutyYiBan);
        List yiban = JSONArray.toList(array1, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, yiban);
    }

    private void judgeAddOrUpdate(ScoreFlow flow1, ScoreDetail detail, List<DutyCodeAndScore> jichu) {
        List<ScoreDetail> listInsert = new ArrayList<>();
        List<ScoreDetail> listUpdate = new ArrayList<>();
        for (int i = 0; i < jichu.size(); i++) {
            ScoreDetail scoreDetail = new ScoreDetail();
            scoreDetail.setFSerialNo(flow1.getSerialno());
            scoreDetail.setDSerialNo(jichu.get(i).getTopicId());
            scoreDetail.setScore(jichu.get(i).getScore());
            ScoreDetail detail1 = detailService.selectDetailBySerialNo(scoreDetail.getDSerialNo(), scoreDetail.getFSerialNo());
            if (detail1 != null) {
                scoreDetail.setSerialNo(detail1.getSerialNo());
                listUpdate.add(scoreDetail);
            } else {
                scoreDetail.setSerialNo(StringUtil.generatorId());
                listInsert.add(scoreDetail);
            }
        }
        if (listInsert.size() > 0) {
            detailService.batchInset(listInsert);
        }
        if (listUpdate.size() > 0) {
            detailService.batchUpdate(listUpdate);
        }
    }


    /**
     * 查询首页待评分人数据列表
     *
     * @param dto
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLike(HttpServletRequest req, String usercode, UserSummaryDto dto) throws ParseException {
        ModelMap map = new ModelMap();
        try {
            List<Role> roles = roleService.selectRoleListByUserCode(usercode);
            if (roles.size() > 0) {
                for (Role role : roles) {
                    if (role.getRolecode().equals("50") || role.getRolecode().equals("100") ||
                            role.getRolecode().equals("150") || role.getRolecode().equals("200") || role.getRolecode().equals("300")) {
                        String state = (String) req.getSession().getAttribute("state");
                        List<UserSummaryDto> summarys = new ArrayList<>();
                        //获取当前年份
                        String year = CalendarUtil.getYear();
                        //获取当前月份
                        String month = CalendarUtil.getMonth();
                        //获取当前季度
                      //  String quarter = CalendarUtil.getQuarter(month);
                        //当前上一个季度
                        int count = Integer.parseInt(month.trim()) - 1;
                        //获取当前系统时间
                        String sysTime = DateUtil.getTime();
                        if (state.equals("1")) {
                            //手动考核-查看所有季节总结
                            manualSelectUserDtoLike(usercode, dto, map, summarys, year, month, count, sysTime);
                        } else {
                            //自动考核-查看所有季节总结
                            automaticSelectUserDtoLike(usercode, dto, map, summarys, year, count);
                        }
                        break;
                    } else {
                        map.put("msg", "该用户不是组织部成员，没有权限进入");
                        map.put("code", 1);
                    }
                }
            } else {
                map.put("msg", "该用户没有角色或用户不存在,请联系管理员");
                map.put("code", 1);
            }
        }catch (Exception e){
            e.printStackTrace();
            map.put("msg", "用户信息异常");
            map.put("code", 812);
        }
        return map;

    }

    private void manualSelectUserDtoLike(String usercode, UserSummaryDto dto, ModelMap map, List<UserSummaryDto> summarys, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                getSummarys(usercode, dto, map, summarys, year, month);
            } else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticSelectUserDtoLike(usercode, dto, map, summarys, year, count);
            }
        } else {
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                getSummarys(usercode, dto, map, summarys, year, month);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                getSummarys(usercode, dto, map, summarys, year, month);
            }
        }
    }

    private void automaticSelectUserDtoLike(String usercode, UserSummaryDto dto, ModelMap map, List<UserSummaryDto> summarys, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";

        } else {
            month = String.valueOf(count);
        }
        try {
            getSummarys(usercode, dto, map, summarys, year, month);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询待评分列表数据失败");
            map.put("code", 1);
        }
    }

    private void getSummarys(String usercode, UserSummaryDto dto, ModelMap map, List<UserSummaryDto> summarys, String year, String month) {
        List<UserSummaryDto> summaryList;
        dto.setMonth(month);
        dto.setYear(year);
        dto.setScorringcode(usercode);
        summaryList = summaryDtoService.selectUserSummaryBySixState(dto);
        for (UserSummaryDto dto1 : summaryList) {
            List<ScoreFlow> flow = flowService.selectByScoreFlow(dto1.getSerialno(), dto1.getScorringcode());
            if (flow.size() == 0) {
                summarys.add(dto1);
            }
        }
        map.put("totalPages", summarys.size());
        map.put("msg", "查询待评分列表数据成功");
        map.put("data", summarys);
        map.put("code", 0);
    }


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
        User user1 = userService.findUserByUserCode(user.getUsercode());
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


    @RequestMapping("/findUserByUserCode")
    public Object findUserByUserCode(String usercode) {
        ModelMap map = new ModelMap();
        try {
            List<Role> roles = roleService.selectRoleListByUserCode(usercode);
            if (roles.size() > 0) {
                boolean isOrganization = false;
                boolean isMedicalEthics = false;
                for (Role role : roles) {
                    if (role.getRolecode().equals("50") || role.getRolecode().equals("100") ||
                        role.getRolecode().equals("150") || role.getRolecode().equals("200") ||
                        role.getRolecode().equals("300")) {
                        //组织部
                        isOrganization = true;
                    }

                    if (role.getRolecode().equals(MEDICAL_ETHICS_USER_ROLE)){
                        //医德医风
                        isMedicalEthics = true;
                    }
                }

                //不是医德医风
                if (!isMedicalEthics){{
                    //是组织部
                    if (isOrganization){
                        User user = userService.findUserByUserCode(usercode);
                        Station station = stationService.selectByStationCode(user.getStationcode());
                        if (station != null) {
                            user.setStationname(station.getStationname());
                            Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
                            if (department != null) {
                                user.setDepartmentname(department.getDepartmentname());
                            }
                        }
                        map.put("data", user);
                        map.put("msg", "查询用户成功");
                        map.put("code", 0);
                    }else {
                        //不是组织部
                        map.put("msg", "该用户不是组织部成员，没有权限进入");
                        map.put("code", 1);
                    }
                }}else {
                    //是组织部
                    if (isOrganization){
                        User user = userService.findUserByUserCode(usercode);
                        Station station = stationService.selectByStationCode(user.getStationcode());
                        if (station != null) {
                            user.setStationname(station.getStationname());
                            Department department = departmentService.selectByDeptCode(station.getDepartmentcode());
                            if (department != null) {
                                user.setDepartmentname(department.getDepartmentname());
                            }
                        }
                        map.put("data", user);
                        map.put("msg", "查询用户成功");
                        map.put("code", 0);
                    }else {
                        //是医德医风
                        User user = userService.getUserByUserCode(usercode);
                        map.put("data", user);
                        map.put("msg", "查询用户成功");
                        map.put("code", 0);
                    }
                }
            } else {
                map.put("msg", "该用户没有角色或用户不存在,请联系管理员");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询用户失败");
            map.put("code", 1);
        }
        return map;

    }


    @RequestMapping(value = "/upload", produces = "application/json;charset=utf-8")
    public Object uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest req, String usercode) {
        ModelMap map = new ModelMap();
        //获取YYYYMMDD格式的日期
        String days = DateUtil.getDays();
        //获取yyyyMMddHHmmss格式的日期
        String times = DateUtil.getSdfTimes();
        String path = req.getSession().getServletContext().getRealPath("/");
        String path1 = path.substring(0, path.lastIndexOf("360kao"));

        //获取路径
        String realPath = path1 + "360check" + File.separator + "picture" + File.separator + "file";
        try {
            //创建文件夹
            File floder = new File(realPath + File.separator + days + File.separator + times);
            if (!floder.isDirectory()) {
                floder.mkdirs();
            }
            //获取文件名称
            String filename = file.getOriginalFilename();
            //保存文件
            File newfile = new File(floder + File.separator + filename);
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(String.valueOf(newfile))));
            out.write(file.getBytes());
            out.flush();
            out.close();
            //文件保存路径
            String picturepath = newfile.getPath().replaceAll("\\\\", "/");
            int i = picturepath.lastIndexOf("picture");
            picturepath = picturepath.substring(i, picturepath.length());
            if (filename.contains(".jpg") || filename.contains(".png") || filename.contains(".jpeg")) {
                if (file.getSize() > 10240000) {
                    map.put("msg", "图片过大,请重新上传");
                    map.put("code", 1);
                } else {
                    User user = userService.findUserByUserCode(usercode);
                    if (user != null) {
                        user.setPicturepath(picturepath);
                        userService.updateByPrimaryKeySelective(user);
                    }
                    map.put("picturepath", picturepath);
                    map.put("msg", "图片上传成功");
                    map.put("code", 0);
                }
            } else {
                map.put("msg", "不支持该格式图片上传，请上传jpg、jepg、png格式的图片");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "图片上传失败");
            map.put("code", 1);
        }

        return map;
    }

    /**
     * 用户登录
     *
     * @param moneycard
     * @param password
     * @return
     */
    @RequestMapping(value = "/login", produces = "application/json;charset=utf-8")
    public Object login(String moneycard, String password, HttpServletRequest req) {
        ModelMap map = new ModelMap();
        String passwordMd5 = MD5.md5(password);//将前端获取的password进行加密与数据库的密码进行比对
        User user = userService.selectUserByMoneyCard(moneycard);
        if (user == null || user.getFlag().equals("1")) {
            map.put("msg", "用户不存在");
            map.put("code", 1);
        } else if (user.getUserstate().equals("0")) {
            map.put("msg", "该用户已被停用,如有疑问,请联系管理员");
            map.put("code", 1);
        } else if (moneycard.equals("") || moneycard == null) {
            map.put("msg", "用户名不能为空");
            map.put("code", 1);
        } else if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            map.put("code", 1);
        } else if (!passwordMd5.equals(user.getPassword())) {
            map.put("msg", "密码错误");
            map.put("code", 1);
        } else {
            List<Role> roles = roleService.selectRoleListByUserCode(user.getUsercode());
            if (roles.size() > 0) {
                boolean existPermission = false;
                for (Role role : roles) {
                    if (role.getRolecode().equals("50") || role.getRolecode().equals("100") ||
                            role.getRolecode().equals("150") || role.getRolecode().equals("200") || role.getRolecode().equals("300") ||role.getRolecode().equals("700")) {
                        existPermission = true;
                        break;
                    }
                }

                if (existPermission){
                    getLoginUserInfo(req, map, user);
                }else {
                    map.put("msg", "该用户不是组织部成员，没有权限进入");
                    map.put("code", 1);
                }
            } else {
                map.put("msg", "该用户没有角色或用户不存在,请联系管理员");
                map.put("code", 1);
            }
        }
        return map;
    }

    private void getLoginUserInfo(HttpServletRequest req, ModelMap map, User user) {
        //将用户编号保存在session中
        req.getSession().setAttribute("usercode", user.getUsercode());
        //设置登录用户的过期时间
        req.getSession().setMaxInactiveInterval(30 * 60);
        //获取手动考核按钮状态
        AssessmentState state = stateService.selectByPrimaryKey(1);
        req.getSession().setAttribute("state", state.getState());
        //查询医德医风的角色列表
        String moneyCard = user.getMoneycard();
        List<MedicalEthicsUser> usersList = medicalEthicsUserService.list(new HashMap<String, String>() {{
            put("userId", moneyCard);
        }});
        if (usersList != null && !usersList.isEmpty()) {
            List<String> roleList= new ArrayList<>();
            usersList.forEach(u ->{
                roleList.add(u.getRoleCode());
            });
            user.setMedicalEthicsRoleList(roleList);
        }
        List<Role> roles = roleService.selectRoleListByUserCode(user.getUsercode());
        if (roles.size() == 1) {
            user.setRolecode(roles.get(0).getRolecode());
            user.setRolename(roles.get(0).getRolecode());
        } else if (roles.size() > 1) {
            user.setRoleList(roles);
            user.setRolecode("");
            user.setRolename("");
        } else {
            user.setRolecode("300");
            user.setRolename("普通用户");
        }
        user.setPassword("****");
        map.put("msg", "登录成功");
        map.put("data", user);
        map.put("code", 0);
    }
}
