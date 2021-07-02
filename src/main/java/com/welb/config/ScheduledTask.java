package com.welb.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserEvaluationDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.personnel_check.entity.PersonnelAuthorization;
import com.welb.personnel_check.service.IAuthorizationUserService;
import com.welb.personnel_check.service.IPersonnelAuthorizationService;
import com.welb.personnel_check.service.IRaterService;
import com.welb.sysBase.service.BatchService;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.HttpUtils;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import com.welb.util.PageData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ScheduledTask
 * @projectName xh-360appraisal-interface
 * @description: 定时任务
 * @date 2019/10/1514:43
 */
@Component
public class ScheduledTask {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IUserService userService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IScoreDetailService detailService;
    @Resource
    IEvaluateDeleteService evaluateDeleteService;
    @Resource
    IResultReportService resultReportService;
    @Resource
    IResultDetailService resultDetailService;
    @Resource
    IUserDtoService dtoService;
    @Resource
    IMonthSummaryService summaryService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IEvaluationReportService evaluationReportService;
    @Resource
    IUserDtoService userDtoService;
    @Resource
    IHistoryScoreService historyScoreService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IAssessmentStateService stateService;
    @Resource
    IPersonnelAuthorizationService authorizationService;
    @Resource
    IRaterService raterService;
    @Resource
    IAuthorizationUserService authorizationUserService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private HUserService service;

    /**
     * 动态获取打分状态
     */
    @Scheduled(cron = "0 0 13,18 * * ?") //每天13点、18点执行一次
    public void getScoreStatusScheduledTask() {
        AssessmentState assessmentState = stateService.selectByPrimaryKey(1);
        String state = assessmentState.getState();
        try {
            String year = CalendarUtil.getYear();
            String month = CalendarUtil.getMonth();
            String quarter = CalendarUtil.getQuarter(month);
            int i = Integer.parseInt(quarter) - 1;
            //获取当前系统时间
            String sysTime = DateUtil.getTime();

                //手动考核-查看所有季节总结
                manualGetStatus(year, quarter, i, sysTime);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
    }

    @Scheduled(cron = "0 30 23 * * ?") //
    public void autoSynHrpData(){
        int upd = 0;
        int add = 0;
        int del = 0;
        ModelMap map = new ModelMap();
        try {
            HttpUtils weath = new HttpUtils();
            String date = DateUtil.getDays();// 获取今天的日期
            //2.同步人事信息
            log.info("开始获取人事信息");
            String[] len2;
            long start = System.currentTimeMillis();
            /**   获取h_distribution表的数据  */
            len2 = getPersonnel(weath, date);
            long end = System.currentTimeMillis();
            log.info("获取人事信息完成,耗时" + (end - start) + "ms");
            Msg msg = new Msg(upd, add, del).invoke(len2);
            map.put("msg", "同步成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("msg", "同步失败");
            map.put("code", 1);
            map.put("error", e.getStackTrace());
            log.error(LogUtil.getTrace(e));
        }

        System.out.println("同步hrp数据结束!");
    }

    private String[] getPersonnel(HttpUtils weath, String date) throws DocumentException {
        String weather = weath.sendGet("http://192.168.64.200/SAPService/HRService.asmx/Get_HR_KYXX?DATE=" + date + "&destinationName=P01_800");
        Document doc = DocumentHelper.parseText(weather);
        Element root = doc.getRootElement();
        String arr = root.getText();
        String[] len = arr.split("\\|");
        return len;
    }

    private void manualGetStatus(String year, String quarter, int i, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
        if (setTime != null) {

                //开始新的季度考核
                month = quarter;
                getUserDto(setTime.getYear(), setTime.getMonth());

        }
    }

    private void getUserDto(String year, String month) {
        UserDto dto = new UserDto();
        dto.setYear(year);
        dto.setMonth(month);
        List<UserDto> userDtoList = dtoService.selectUserDtoLike(dto);
        getStationName(userDtoList);
    }

    private void automaticGetStatus(String year, int i) {
        String month;
        if (i == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "4";
        } else {
            month = String.valueOf(i);
        }
        getUserDto(year, month);
    }

    /**
     * 获取人员相关信息 如岗位 部门 分数
     *
     * @param userDtoList
     */
    private void getStationName(List<UserDto> userDtoList) {
        //保留两位小数
        List<MonthSummary> list = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            MonthSummary summary = new MonthSummary();
            summary.setSerialno(userDto.getSerialno());
            userDto.setEmployeecode(userDto.getUsercode());
            List<UserRoleKey> roles = userRoleService.selectUserRoleByUserCode(userDto.getUsercode());
            for (UserRoleKey role : roles) {
                if (role.getRolecode().equals("150")) {
                    summary.setState("");
                } else {
                    summary.setState(userDto.getState());

                }
            }
            int dtoTotalCount = dtoService.getTotalCount(userDto);
            String mserialno = userDto.getYear() + "-" + userDto.getMonth();
            int flowTotalCount = flowService.getTotalCount(mserialno, userDto.getUsercode());
            if (flowTotalCount == 0) {
                summary.setScorestatus("1");
                list.add(summary);
            } else if (flowTotalCount < dtoTotalCount) {
                summary.setScorestatus("2");
                list.add(summary);
            } else {
                summary.setScorestatus("3");
                list.add(summary);
            }


        }
        if (list.size() > 0) {
            summaryService.batchUpdate(list);
        }

    }

    /**
     * 删除数据垃圾数据
     */
    //  定时任务时间表达式
   /* @Scheduled(cron = "0 15 12 10 * ?") //每月10号12点15分钟执行任务
    public void deleteDataScheduledTask() {
        //这里是对某个代码段或者某个功能进行定时操作
        List<User> users = userService.findFlagUsers();
        for (User user : users) {
            //删除scoreflow表与用户相关的数据
            List<ScoreFlow> scorringFlows = flowService.selectFlowByScorringCode(user.getUsercode());
            List<String> scorringSerialnos = new ArrayList<>();
            deleteFlowAndDetail(scorringFlows, scorringSerialnos);
            List<ScoreFlow> scorredFlows1 = flowService.selectFlowByScorredCode(user.getUsercode());
            List<String> scorredSerialnos = new ArrayList<>();
            deleteFlowAndDetail(scorredFlows1, scorredSerialnos);

        }
        List<EvaluateDelete> list = evaluateDeleteService.selectAllEvaluateDelete();
        if (list.size() > 0) {
            List<Integer> evaluateIds = new ArrayList<>();
            List<Integer> resultReportIds = new ArrayList<>();
            List<Integer> resultDetailIds = new ArrayList<>();
            for (EvaluateDelete evaluateDelete : list) {
                evaluateIds.add(evaluateDelete.getEvaluateid());
                List<ResultReport> resultReports = resultReportService.selectResultReportByEvaluationCode(evaluateDelete.getEvaluateid());
                if (resultReports.size() > 0) {
                    for (ResultReport resultReport : resultReports) {
                        resultReportIds.add(resultReport.getId());
                        //删除resultDetail数据
                        List<ResultDetail> details = resultDetailService.selectResultDetailByReportCode(resultReport.getId());
                        if (details.size() > 0) {
                            for (ResultDetail detail : details) {
                                resultDetailIds.add(detail.getId());
                            }
                        }
                    }
                }
            }
            if (resultDetailIds.size() > 0) {
                resultDetailService.batchDelete(resultDetailIds);
            }
            if (resultReportIds.size() > 0) {

                resultReportService.batchDelete(resultReportIds);
            }
            if (evaluateIds.size() > 0) {
                evaluateDeleteService.batchDelete(evaluateIds);
            }
        }
    }

    private void deleteFlowAndDetail(List<ScoreFlow> scorredFlows1, List<String> scorredSerialnos) {
        if (scorredFlows1.size() > 0) {
            List<String> detailSerialNos = new ArrayList<>();
            for (ScoreFlow flow : scorredFlows1) {
                scorredSerialnos.add(flow.getSerialno());
                List<ScoreDetail> scoreDetails = detailService.selectSerialNoByFSerialNo(flow.getSerialno());
                if (scoreDetails.size() > 0) {
                    for (ScoreDetail detail : scoreDetails) {
                        detailSerialNos.add(detail.getSerialNo());
                    }
                }
            }
            detailService.batchDelete(detailSerialNos);
            flowService.batchDelete(scorredSerialnos);
        }

    }
*/

    @Scheduled(cron = "0 0 2 * * ?")//每天凌晨2点执行一次
    public void getEvaluateMaxScoreAndMinScore() {
        AssessmentState assessmentState = stateService.selectByPrimaryKey(1);
        String state = assessmentState.getState();
        UserDto dto = new UserDto();
        UserEvaluationDto evaluationDto = new UserEvaluationDto();
        try {
            String year = "";
            String month ="";


                //手动考核-查看所有季节总结
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");

            evaluationDto.setYear(setTime.getYear());
            evaluationDto.setMonth(setTime.getMonth());

            year= setTime.getYear();
            month= setTime.getMonth();

            List<UserEvaluationDto> evaluationReports;
            evaluationReports = evaluationReportService.selectAllEvaluationReportLike(evaluationDto);
            if (evaluationReports.size() > 0) {
                for (UserEvaluationDto evaluation : evaluationReports) {
                    if (evaluation.getMaxscore() == null && evaluation.getMinscore() == null) {
                        dto.setYear(evaluation.getYear());
                        dto.setMonth(evaluation.getMonth());
                        dto.setUsername(evaluation.getUsername());
                        dto.setUsercode(evaluation.getUsercode());
                        List<UserDto> userDtos = userDtoService.selectUserDtoLike(dto);
                        //获取岗位信息
                        getStationNameByEvaluate(userDtos, dto, evaluationDto);
                        //获取各类评分的分数
                        getScoreTypeMarks(evaluation, userDtos);
                        PageInfo<UserEvaluationDto> pageInfo = new PageInfo<>(evaluationReports);
                        evaluationReports = pageInfo.getList();
                      /*  DecimalFormat formart = new DecimalFormat("0.00");
                        //计算整体平均分
                        Double totalScore = evaluationReportService.selectReportTotalScore(year, month);

                        int reportCount = evaluationReportService.selectReportCount(year, month);
                        double avgScore = totalScore / reportCount;
                        Double formatAvgScore = Double.valueOf(formart.format(avgScore));
                        evaluation.setAvgscore(formatAvgScore);*/
                        //填充evaluation信息
                        EvaluationReport report = getEvaluationReport(evaluation);
                        report.setAvgscore(evaluation.getAvgscore());
                        //            第三步  获取最高分和最低分
                        report.setUsercode(evaluation.getUsercode());
                        Double maxScore = flowService.selectMaxScoreByScoredCode(report.getUsercode(), year, month);
                        Double minScore = flowService.selectMinScoreByScoredCode(report.getUsercode(), year, month);
                        report.setMaxscore(maxScore);
                        report.setMinscore(minScore);
//            第四步  计算分值差
                        double scoreDifference = maxScore - minScore;
                        report.setScoredifference(scoreDifference);
                        EvaluationReport report1 = new EvaluationReport();
                        report1.setUsercode(report.getUsercode());

                            report1.setYear(year);
                            report1.setMonth(month);
                            EvaluationReport report2 = evaluationReportService.selectReportByUserCode(report1);
                            getCompareLastInfo(report, maxScore, minScore, report2);

                        evaluationReportService.updateByPrimaryKeySelective(report);
                    }
                }

            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
    }

    private void getCompareLastInfo(EvaluationReport report, Double maxScore, Double minScore, EvaluationReport report2) {
        if (report2 == null) {
            report.setMaxcomparelast(0.0);
            report.setMincomparelast(0.0);
            report.setTotalcomparelast(0.0);
        } else {
            if (report2.getMinscore() != null && report2.getMaxscore() != null) {
                double maxCompareLast = maxScore - report2.getMaxscore();
                double minCompareLast = minScore - report2.getMinscore();
                double totalCompareLast = (report.getTotalscore() - report2.getTotalscore()) / report2.getTotalscore();
                report.setMaxcomparelast(maxCompareLast);
                report.setMincomparelast(minCompareLast);
                report.setTotalcomparelast(totalCompareLast * 100);
            } else {
                report.setMaxcomparelast(0.0);
                report.setMincomparelast(0.0);
                report.setTotalcomparelast(0.0);
            }
        }
    }


    private EvaluationReport getEvaluationReport(UserEvaluationDto evaluation) {
        EvaluationReport report = new EvaluationReport();
        report.setId(evaluation.getId());
        report.setAscore(evaluation.getAscore());
        report.setBscore(evaluation.getBscore());
        report.setCscore(evaluation.getCscore());
        report.setDscore(evaluation.getDscore());
        report.setDepartmentname(evaluation.getDepartmentname());
        report.setStationname(evaluation.getStationname());
        report.setTotalscore(evaluation.getTotalscore());
        report.setAvgscore(evaluation.getAvgscore());
        return report;
    }


    private void getScoreTypeMarks(UserEvaluationDto evaluation, List<UserDto> userDtos) {
        if (userDtos.size() > 0) {
            for (UserDto dto1 : userDtos) {
                if (dto1.getUsercode().equals(evaluation.getUsercode())) {
                    evaluation.setTotalscore(dto1.getTotalScore());
                    evaluation.setAscore(dto1.getAScore());
                    evaluation.setBscore(dto1.getBScore());
                    evaluation.setCscore(dto1.getCScore());
                    evaluation.setDscore(dto1.getDScore());
                    evaluation.setStationname(dto1.getStationname());
                    evaluation.setDepartmentname(dto1.getDepartmentname());

                }
            }
        } else {
            evaluation.setAscore(0.0);
            evaluation.setBscore(0.0);
            evaluation.setCscore(0.0);
            evaluation.setDscore(0.0);
        }
    }

    private void getStationNameByEvaluate(List<UserDto> userDtoList, UserDto dto, UserEvaluationDto evaluationDto) {
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            for (UserDto userDto : userDtoList) {
                if (userDto.getUsercode().equals(dto.getUsercode())) {
                    //获取各类评分的总人数和分数
                    Double Ascore = flowService.getScoreByType(userDto.getSerialno(), "A");
                    int Acount = flowService.getScoreByTypeCount(userDto.getSerialno(), "A");
                    Double totalRatio = 0.0;
                    Double Ascore1;
                    if (Ascore == null) {
                        Ascore1 = 0.0;
                        userDto.setAScore(Ascore1);
                    } else {
                        Ascore1 = Ascore / Acount;//A类总分/A类人数
                        userDto.setAScore(Double.parseDouble(df.format(Ascore1)));
                        totalRatio += userDto.getAratio();
                    }
                    Double Bscore = flowService.getScoreByType(userDto.getSerialno(), "B");
                    int Bcount = flowService.getScoreByTypeCount(userDto.getSerialno(), "B");
                    Double Bscore1;
                    if (Bscore == null) {
                        Bscore1 = 0.0;
                        userDto.setBScore(Bscore1);
                    } else {
                        Bscore1 = Bscore / Bcount;
                        userDto.setBScore(Double.parseDouble(df.format(Bscore1)));
                        totalRatio += userDto.getBratio();
                    }
                    Double Cscore = flowService.getScoreByType(userDto.getSerialno(), "C");
                    int Ccount = flowService.getScoreByTypeCount(userDto.getSerialno(), "C");
                    Double Cscore1;
                    if (Cscore == null) {
                        Cscore1 = 0.0;
                        userDto.setCScore(Cscore1);
                    } else {
                        Cscore1 = Cscore / Ccount;
                        userDto.setCScore(Double.parseDouble(df.format(Cscore1)));
                        totalRatio += userDto.getCratio();
                    }
                    Double Dscore = flowService.getScoreByType(userDto.getSerialno(), "D");
                    int Dcount = flowService.getScoreByTypeCount(userDto.getSerialno(), "D");
                    Double Dscore1;
                    if (Dscore == null) {
                        Dscore1 = 0.0;
                        userDto.setDScore(Dscore1);
                    } else {
                        Dscore1 = Dscore / Dcount;
                        userDto.setDScore(Double.parseDouble(df.format(Dscore1)));
                        totalRatio += userDto.getDratio();
                    }
                    //获取总分  总分= A类总分 X A类评分人系数 +  B类总分 X B类评分人系数 + C类总分 X C类评分人系数 + D类总分 X D类评分人系数

                    Double totalscore = userDto.getAScore() * userDto.getAratio() + userDto.getBScore() * userDto.getBratio() +
                            userDto.getCScore() * userDto.getCratio() + userDto.getDScore() * userDto.getDratio();
                    if (totalscore == 0.0 || totalscore == null) {
                        userDto.setTotalScore(0.0);
                    } else {
                        userDto.setTotalScore(Double.parseDouble(df.format(totalscore / totalRatio)));
                    }
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
    }

    @Scheduled(cron = "0 45 17 ? * MON ")//每周一12点30分触发；
    public void monthScoreScheduled() {
        AssessmentState assessmentState = stateService.selectByPrimaryKey(1);
        String state = assessmentState.getState();
        try {
            UserDto dto = new UserDto();
            List<HistoryScore> list = new ArrayList<>();
            String year = CalendarUtil.getYear();
            String month = CalendarUtil.getMonth();
            String quarter = CalendarUtil.getQuarter(month);
            int count = Integer.parseInt(quarter) - 1;
            //获取当前系统时间
            String sysTime = DateUtil.getTime();

                //手动考核-查看所有季节总结
                manualGetUserDto(dto, year, quarter, count, sysTime);

            List<UserDto> userDtoList = dtoService.selectUserDtoLike(dto);
            getMonthScoreStationName(userDtoList);
            List<HistoryScore> scores = historyScoreService.selectAll(dto.getYear(), dto.getMonth());
            addMonthScore(list, userDtoList, scores);
            if (list.size() > 0) {
                historyScoreService.batchInsert(list);
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void manualGetUserDto(UserDto dto, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
        if (setTime != null) {

                //开始新的季度考核
                month = quarter;
                dto.setYear(setTime.getYear());
                dto.setMonth(setTime.getMonth());


        }
    }

    private void automaticGetUserDto(UserDto dto, String year, int count) {
        String month;
        if (count == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "4";
        } else {
            month = String.valueOf(count);
        }
        dto.setYear(year);
        dto.setMonth(month);
    }

    private void addMonthScore(List<HistoryScore> list, List<UserDto> userDtoList, List<HistoryScore> scores) {
        if (scores.size() > 0) {
            List<Integer> ids = new ArrayList<>();
            for (HistoryScore score : scores) {
                ids.add(score.getId());
            }
            if (ids.size() > 0) {
                historyScoreService.batchDelete(ids);
            }
        }
        for (UserDto userDto : userDtoList) {
            HistoryScore score = new HistoryScore();
            score.setYear(userDto.getYear());
            score.setMonth(userDto.getMonth());
            score.setAscore(userDto.getAScore());
            score.setBscore(userDto.getBScore());
            score.setCscore(userDto.getCScore());
            score.setDscore(userDto.getDScore());
            score.setTotalscore(userDto.getTotalScore());
            score.setUsername(userDto.getUsername());
            score.setDepartmentname(userDto.getDepartmentname());
            score.setRolecode(userDto.getRolecode());
            score.setStationcode(userDto.getStationcode());
            score.setScorestatus(userDto.getScorestatus());
            score.setState(userDto.getState());
            score.setMoneycard(userDto.getMoneycard());
            score.setStationname(userDto.getStationname());
            list.add(score);
        }
    }

    private void getMonthScoreStationName(List<UserDto> userDtoList) {
        //保留两位小数
        DecimalFormat df = new DecimalFormat("0.00");
        try {
            for (UserDto userDto : userDtoList) {
                if (!userDto.getRolecode().equals("150")) {
                    Double totalRatio = 0.0;
                    Double AScore = flowService.getTypeAvgScore(userDto.getSerialno(), "A");
                    if (AScore == null) {
                        AScore = 0.0;
                        userDto.setAScore(AScore);
                    } else {
                        userDto.setAScore(Double.parseDouble(df.format(AScore)));
                        totalRatio += userDto.getAratio();
                    }
                    Double BScore = flowService.getTypeAvgScore(userDto.getSerialno(), "B");
                    if (BScore == null) {
                        BScore = 0.0;
                        userDto.setBScore(BScore);
                    } else {
                        userDto.setBScore(Double.parseDouble(df.format(BScore)));
                        totalRatio += userDto.getBratio();
                    }
                    Double CScore = flowService.getTypeAvgScore(userDto.getSerialno(), "C");
                    if (CScore == null) {
                        CScore = 0.0;
                        userDto.setCScore(CScore);
                    } else {
                        userDto.setCScore(Double.parseDouble(df.format(CScore)));
                        totalRatio += userDto.getCratio();
                    }
                    Double DScore = flowService.getTypeAvgScore(userDto.getSerialno(), "D");
                    if (DScore == null) {
                        DScore = 0.0;
                        userDto.setDScore(DScore);
                    } else {
                        userDto.setDScore(Double.parseDouble(df.format(DScore)));
                        totalRatio += userDto.getDratio();
                    }
                    //获取总分  总分= A类总分 X A类评分人系数 +  B类总分 X B类评分人系数 + C类总分 X C类评分人系数 + D类总分 X D类评分人系数

                    Double totalscore = userDto.getAScore() * userDto.getAratio() + userDto.getBScore() * userDto.getBratio() +
                            userDto.getCScore() * userDto.getCratio() + userDto.getDScore() * userDto.getDratio();
                    if (totalscore == 0.0 || totalscore == null) {
                        userDto.setTotalScore(0.0);
                    } else {
                        userDto.setTotalScore(Double.parseDouble(df.format(totalscore / totalRatio)));
                    }
                } else {
                    userDto.setAScore(0.0);
                    userDto.setBScore(0.0);
                    userDto.setCScore(0.0);
                    userDto.setDScore(0.0);
                    userDto.setTotalScore(0.0);
                }
            }

        } catch (Exception e) {
            e.getStackTrace();
        }

    }

    /**
     * 人事部代理人开始考核
     */
    @Scheduled(cron = "0 0 0,5 * * ?") //每天0点、5点执行一次
    public void openAuthorization() {
        List<UserRoleKey> list = new ArrayList<>();
        List<String> agents = new ArrayList<>();
        //获取当前系统时间
        String starttime = DateUtil.getTime();
        System.err.println("定时任务开始啦" + starttime);
        List<PersonnelAuthorization> authorizations = authorizationService.selectListByStart(starttime);
        if (authorizations.size() > 0) {
            for (PersonnelAuthorization authorization : authorizations) {
                UserRoleKey key = getUserRoleKey(authorization);
                list.add(key);
                agents.add(authorization.getAgent());
                authorizationUserService.updateFlag("2", authorization.getAgent());
            }
        }
        if (list.size() > 0) {
            userRoleService.batchInsert(list);
        }
        if (agents.size() > 0) {
            authorizationService.batchUpdate(agents);
        }
        String end = DateUtil.getTime();
        System.out.println("定时任务结束啦" + end);

    }

    private UserRoleKey getUserRoleKey(PersonnelAuthorization authorization) {
        UserRoleKey key = new UserRoleKey();
        key.setRolecode("500");
        User user = userService.selectByMoneyCard(authorization.getAgent());
        key.setUsercode(user.getUsercode());
        return key;
    }

    /**
     * 人事部代理人结束考核
     */
    @Scheduled(cron = "0 0 0,5 * * ?") //每天0点、5点执行一次
    public void closeAuthorization() {
        //获取当前系统时间
        String endtime = DateUtil.getTime();
        System.err.println("定时任务开始啦" + endtime);
        List<PersonnelAuthorization> authorizations = authorizationService.selectListByLater(endtime);
        if (authorizations.size() > 0) {
            for (PersonnelAuthorization authorization : authorizations) {
                //修改人事部代理人代理状态
                authorizationUserService.updateFlagByMoneyCard("3", authorization.getAgent());
                User user = userService.selectByMoneyCard(authorization.getAgent());
                //删除代理人的角色
                deleteRole(user);
                if (user.getRoletype().equals("0")) {
                    user.setIsagent("0");
                    userService.updateByPrimaryKeySelective(user);
                } else {
                    userService.deleteUserByMoneyCard(user.getMoneycard());
                }
                //删除人事部用户管理中的代理人
                raterService.deleteRaterByScorringCode(authorization.getAgent());
                //修改人事部授权表的状态
                authorizationService.updateFlag("4",endtime,authorization.getAgent());
            }
        }
        String end = DateUtil.getTime();
        System.out.println("定时任务结束啦" + end);

    }

    private void deleteRole(User user) {
        UserRoleKey key = new UserRoleKey();
        key.setRolecode("500");
        key.setUsercode(user.getUsercode());
        userRoleService.deleteByPrimaryKey(key);
    }

    private class Msg {
        private int upd;
        private int add;
        private int del;

        public Msg(int upd, int add, int del) {
            this.upd = upd;
            this.add = add;
            this.del = del;
        }

        public int getUpd() {
            return upd;
        }

        public int getAdd() {
            return add;
        }

        public int getDel() {
            return del;
        }

        /**
         * 开始更新人事信息到数据库
         */
        public Msg invoke(String[] len) {
            //只同步h_user表
            List<PageData> updateList = new ArrayList<>();
            List<PageData> insertList = new ArrayList<>();
            List<String> allIds = service.findAllUids();//本地 发薪号
            List<String> hrpIds = new ArrayList<>();//hrp 发薪号
            List<PageData> delList = new ArrayList<>();//要删除的发薪号
            for (int i = 0; i < len.length; i++) {
                PageData pd = new PageData();
                String[] as = len[i].split(",");
                for (int j = 0; j < as.length; j++) {
                    pd.put("pd" + j, as[j]);
                }
                hrpIds.add(pd.getString("pd0"));
                PageData is2 = service.findUid2(pd.getString("pd0"));// 判断人事信息是否存在
                if (is2 == null) {
                    insertList.add(pd);
                } else {
                    updateList.add(pd);
                }
            }
            //这里处理删除逻辑
            for (int i = 0; i < allIds.size(); i++) {
                if (!hrpIds.contains(allIds.get(i))) {//如果hrp中不包括本地的发薪号就删除掉本地的
                    PageData pageData = new PageData();
                    pageData.put("u_id", allIds.get(i));
                    delList.add(pageData);
                    del++;
                }
            }
            if (!delList.isEmpty()) {//删除非hrp中的本地h_user的数据
                batchService.doBatch(delList, "com.welb.sysBase.mapper.PerconneMapper.delDistribution");
            }
            if (!updateList.isEmpty()) {//根据hrp获取的信息,更新本地h_user中的其他字段信息
                batchService.doBatch(updateList, "com.welb.sysBase.mapper.PerconneMapper.upd2");
            }
            if (!insertList.isEmpty()) {//增加hrp中的新用户到h_user
                batchService.doBatch(insertList, "com.welb.sysBase.mapper.PerconneMapper.save2");
            }
            return this;
        }
    }
}


