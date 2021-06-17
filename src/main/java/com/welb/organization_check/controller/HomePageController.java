package com.welb.organization_check.controller;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserSummaryDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import com.welb.util.StringUtil;
import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author luoyaozu
 * @title: HomePageController
 * @projectName xh-360appraisal-interface
 * @description: 首页控制器
 * @date 2019/6/1122:02
 */
@RestController
@RequestMapping("/homepage")
@Transactional
public class HomePageController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IUserSummaryDtoService dtoService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IDutyService dutyService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IScoreDetailService detailService;
    @Resource
    IScoreService scoreService;
    @Resource
    IUserService userService;
    @Resource
    IEvaluationReportService evaluationReportService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IScoreHistoryService historyService;
    @Resource
    IUserDtoService userDtoService;

    /**
     * 个人考核详情页面
     *
     * @return
     */
    @RequestMapping(value = "/getDetail", produces = "application/json;charset=utf-8")
    public Object getDetail(HttpServletRequest req, UserSummaryDto dtos) {
        //获取当前登录用户的编号
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            UserSummaryDto dto;
            if (usercode == null) {
                map.put("msg", "用户登录过期，请重新登录");
                map.put("code", 810);
            } else {
                try {
                    if (dtos.getMonth() != null && dtos.getYear() != null) {
                        dto = dtoService.selectUserSummaryByLike(dtos);
                        if (dto != null) {
                            //查找岗位
                            dto.setMonth(dtos.getMonth());
                            getMapList(map, usercode, data, dto);
                        } else {
                            map.put("msg", "数据为空");
                            map.put("code", 0);
                        }
                    } else {
//                    当前年份
                        String year = CalendarUtil.getYear();
//                    当前月份h
                        String month = CalendarUtil.getMonth();
//                    当前季度
                        String quarter = CalendarUtil.getQuarter(month);
                        //当前上一季度
                        int count = Integer.parseInt(quarter.trim()) - 1;
                        //获取当前系统时间
                        String sysTime = DateUtil.getTime();
                        if (state.equals("1")) {
                            //手动考核  --个人考核详情页面
                            manualGetDetail(dtos, map, usercode, data, year, quarter, count, sysTime);
                        } else {
                            //自动考核  --个人考核详情页面
                            automaticGetDetail(dtos, map, usercode, data, year, count);
                        }
                    }
                } catch (Exception e) {
                    log.error(LogUtil.getTrace(e));
                    map.put("msg", "查询个人考核详情失败");
                    map.put("code", 1);
                }
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getMapList(ModelMap map, String usercode, Map<String, Object> data, UserSummaryDto dto) {
        Station station = stationService.selectByStationCode(dto.getStationcode());
        //获取评分人给被评分人打分的情况
        List<ScoreFlow> flow = flowService.selectByScoreFlow(dto.getSerialno(), usercode);
        getFlow(map, data, dto, station, flow);
    }

    private void manualGetDetail(UserSummaryDto dtos, ModelMap map, String usercode, Map<String, Object> data, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        UserSummaryDto dto;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            //新一季度考核-手动设置的考核时间未超过系统自动考核时间
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的季度考核
                month = quarter;
                dtos.setYear(year);
                dtos.setMonth(month);
                dto = dtoService.selectUserSummaryByLike(dtos);
                dto.setMonth(month);
                //查找岗位
                getMapList(map, usercode, data, dto);
            } else {
                //未到达指定考核时间，仍展示上一季度数据
                automaticGetDetail(dtos, map, usercode, data, year, count);
            }
        } else {
            //新一季度考核-手动设置的考核时间超过系统自动考核时间
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "4";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                dtos.setYear(year);
                dtos.setMonth(month);
                dto = dtoService.selectUserSummaryByLike(dtos);
                if (dto != null) {
                    //查找岗位
                    getMapList(map, usercode, data, dto);
                } else {
                    map.put("msg", "数据为空");
                    map.put("code", 0);
                }
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 4;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                dtos.setYear(year);
                dtos.setMonth(month);
                dto = dtoService.selectUserSummaryByLike(dtos);
                //查找岗位
                getMapList(map, usercode, data, dto);
            }
        }
    }

    private void automaticGetDetail(UserSummaryDto dtos, ModelMap map, String usercode, Map<String, Object> data, String year, int count) {
        String month;
        UserSummaryDto dto;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "4";
        } else {
            month = String.valueOf(count);
        }
        dtos.setYear(year);
        dtos.setMonth(month);
        dto = dtoService.selectUserSummaryByLike(dtos);
        dto.setMonth(month);
        //查找岗位
        getMapList(map, usercode, data, dto);
    }

    private void getFlow(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow) {
        if (scoreFlow.size() > 0) {
            for (ScoreFlow flow : scoreFlow) {
                data.put("total", flow.getScore() + "分");
                //获取基础量化指标相关信息
                List<Duty> dutyJichu = dutyService.queryJiChu(dto.getStationcode());
                getDutyInfo(flow, dutyJichu);
                //获取关键量化指标的相关信息
                List<Duty> dutyYiban = dutyService.queryYiBan(dto.getStationcode());
                getDutyInfo(flow, dutyYiban);
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

    private void getDutyInfo(ScoreFlow flow, List<Duty> dutyJichu) {
        for (Duty duty : dutyJichu) {
            ScoreDetail detail = detailService.selectDetailBySerialNo(duty.getDutycode(), flow.getSerialno());
            if (detail != null) {
                duty.setScore(detail.getScore());
            } else {
                duty.setScore("");
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
/*========================计算完总分之后  个人测评报告也相应的产生一条数据==================================================*/
            getEvaluationReportInfo(req, dto, report);

/*========================被打分用户分数添加或更新  往历史评分数据表插数据==================================================*/
            addOrUpdateScoreHistory(dto, year, month, df, user);
/*=========================打分用户的评分状态更新==========================================================================*/
            updateScoreStatus(scorringcode, year, month);
            map.put("msg", "操作成功");
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "操作失败");
            map.put("error", e.getMessage());
            map.put("eor", e.getStackTrace());
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


    private void getEvaluationReportInfo(HttpServletRequest req, UserSummaryDto dto, EvaluationReport report) throws ParseException {
        String state = (String) req.getSession().getAttribute("state");
        report.setUsercode(dto.getEmployeecode());
//            年份
        String year = CalendarUtil.getYear();
//            月份
        String month = CalendarUtil.getMonth();
//            季度
        String quarter = CalendarUtil.getQuarter(month);
        int count = Integer.parseInt(quarter) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();
        //手动考核-计算总分
        if (state.equals("1")) {
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
            if (setTime != null) {

                //新一季度考核-手动设置的考核时间未超过系统自动考核时间
                if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                    //开始新的季度考核
                    month = quarter;
                    insertOrUpdateEvaluationReport(report, year, month);
                } else {
                    //未到达指定考核时间，仍展示上一季度数据
                    automaticGetEvaluationInfo(report, year, count);
                }
            } else {
                //新一季度考核-手动设置的考核时间超过系统自动考核时间
                if (count == 0) {
                    int lastyear = Integer.parseInt(year.trim()) - 1;
                    year = String.valueOf(lastyear);
                    month = "4";
                } else {
                    month = String.valueOf(count);
                }
                ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
                if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                    insertOrUpdateEvaluationReport(report, year, month);
                } else {
                    int lastMonth = Integer.parseInt(month) - 1;
                    if (lastMonth == 0) {
                        lastMonth = 4;
                        int lastYear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastYear);
                    }
                    month = String.valueOf(lastMonth);
                    insertOrUpdateEvaluationReport(report, year, month);
                }

            }
        } else {
            //自动考核-计算总分
            automaticGetEvaluationInfo(report, year, count);
        }
    }

    private void insertOrUpdateEvaluationReport(EvaluationReport report, String year, String month) {
        EvaluationReport evaluationReport;
        report.setYear(year);
        report.setMonth(month);
        evaluationReport = evaluationReportService.selectReportByUserCode(report);
        if (evaluationReport == null) {
            evaluationReportService.insertSelective(report);
        } else {
            report.setId(evaluationReport.getId());
            evaluationReportService.updateByPrimaryKeySelective(report);
        }
    }

    private void automaticGetEvaluationInfo(EvaluationReport report, String year, int count) {
        String month;
        EvaluationReport evaluationReport;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "4";
        } else {
            month = String.valueOf(count);
        }
        insertOrUpdateEvaluationReport(report, year, month);
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


}
