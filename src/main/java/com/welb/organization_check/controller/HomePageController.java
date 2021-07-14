package com.welb.organization_check.controller;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserSummaryDto;
import com.welb.organization_check.dto.UserScoreDto;
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
import java.util.stream.Collectors;


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
    @Autowired
    IUserScoreDtoService userScoreDtoService;
    @Autowired
    IMonthSummaryService monthSummaryService;

    /**
     * 个人考核详情页面
     *
     * @return
     */
    @RequestMapping(value = "/getDetail", produces = "application/json;charset=utf-8")
    public Object getDetail(HttpServletRequest req, UserSummaryDto dtos,String scorringUserCode) {
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
                            getMapList(map, usercode, data, dto,scorringUserCode);
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
                        String quarter = month;//CalendarUtil.getQuarter(month);
                        //当前上一季度
                        int count = Integer.parseInt(month.trim()) - 1;
                        //获取当前系统时间
                        String sysTime = DateUtil.getTime();

                            //手动考核  --个人考核详情页面
                        manualGetDetail(dtos, map, usercode, data, year, quarter, count, sysTime);

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

    private void getMapList(ModelMap map, String usercode, Map<String, Object> data, UserSummaryDto dto,String scorringUserCode) {
        //获取评分人给被评分人打分的情况
        Station station = new Station();
        List<ScoreFlow> flow = new ArrayList<>();
        if(scorringUserCode!=null && !scorringUserCode.equals("")){
            flow = flowService.selectByScoreFlow(dto.getSerialno(), scorringUserCode);
        }else {
            station = stationService.selectByStationCode(dto.getStationcode());
            flow = flowService.selectByScoreFlow(dto.getSerialno(), usercode);
        }
        getFlow(map, data, dto, station, flow);
    }

    private void manualGetDetail(UserSummaryDto dtos, ModelMap map, String usercode, Map<String, Object> data, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        UserSummaryDto dto;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
        if (setTime != null) {
            //新一季度考核-手动设置的考核时间未超过系统自动考核时间

                //开始新的季度考核
                month = quarter;
                dtos.setYear(setTime.getYear());
                dtos.setMonth(setTime.getMonth());
                dto = dtoService.selectUserSummaryByLike(dtos);
                dto.setMonth(setTime.getMonth());
                //查找岗位
                getMapList(map, usercode, data, dto,null);


        }
    }

    private void automaticGetDetail(UserSummaryDto dtos, ModelMap map, String usercode, Map<String, Object> data, String year, int count) {
        String month;
        UserSummaryDto dto;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(count);
        }
        dtos.setYear(year);
        dtos.setMonth(month);
        dto = dtoService.selectUserSummaryByLike(dtos);
        dto.setMonth(month);
        //查找岗位
        getMapList(map, usercode, data, dto,null);
    }

    private void getFlow(ModelMap map, Map<String, Object> data, UserSummaryDto dto, Station station, List<ScoreFlow> scoreFlow) {
        if (scoreFlow.size() > 0) {
            for (ScoreFlow flow : scoreFlow) {
                data.put("total", flow.getScore() + "分");
                //获取基础量化指标相关信息
                List<Duty> dutyJichu = dutyService.queryDutyByType("0",dto.getStationcode());
                getDutyInfo(flow, dutyJichu);
                //获取关键量化指标的相关信息
                List<Duty> dutyYiban = dutyService.queryDutyByType("1",dto.getStationcode());
                getDutyInfo(flow, dutyYiban);
                //获取关键量化指标的相关信息
                List<Duty> dutyZhongdian = dutyService.queryDutyByType("2",dto.getStationcode());
                getDutyInfo(flow, dutyZhongdian);
                //获取关键量化指标的相关信息
                List<Duty> dutyMubiao = dutyService.queryDutyByType("3",dto.getStationcode());
                getDutyInfo(flow, dutyMubiao);
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
                data.put("dutyZhongdian", dutyZhongdian);
                data.put("dutyMubiao", dutyMubiao);
                map.put("msg", "查询个人考核详情成功");
                map.put("data", data);
                map.put("code", 0);
            }
        } else {
            data.put("total", "");
            List<Duty> dutyJichu = dutyService.queryDutyByType("0",dto.getStationcode());
            for (Duty duty : dutyJichu) {
                duty.setScore("");
            }
            //获取关键量化指标的相关信息
            List<Duty> dutyYiban = dutyService.queryDutyByType("1",dto.getStationcode());
            for (Duty duty : dutyYiban) {
                duty.setScore("");
            }
            //获取关键量化指标的相关信息
            List<Duty> dutyZhongdian = dutyService.queryDutyByType("2",dto.getStationcode());
            for (Duty duty : dutyZhongdian) {
                duty.setScore("");
            }

            //获取关键量化指标的相关信息
            List<Duty> dutyMubiao = dutyService.queryDutyByType("3",dto.getStationcode());
            for (Duty duty : dutyMubiao) {
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
            data.put("dutyZhongdian", dutyZhongdian);
            data.put("dutyMubiao", dutyMubiao);

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
                                String dutyYiban, String dutyZhongdian, String dutyMubiao, EvaluationReport report, String year, String month) {
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
                    getScore(dutyJiChu, dutyYiban, dutyZhongdian, dutyMubiao, scoreFlow);
                }
            } else {
                flowService.insertSelective(flow);
                getScore(dutyJiChu, dutyYiban, dutyZhongdian, dutyMubiao, flow);
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
        String quarter =month;// CalendarUtil.getQuarter(month);
        int count = Integer.parseInt(quarter) - 1;
        //获取当前系统时间
        String sysTime = DateUtil.getTime();
        //手动考核-计算总分
        if (state.equals("1")) {
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
            if (setTime != null) {

                //新一季度考核-手动设置的考核时间未超过系统自动考核时间

                    //开始新的季度考核
                    month = quarter;
                    insertOrUpdateEvaluationReport(report, setTime.getYear(), setTime.getMonth());

            }
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
            month = "12";
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

    private void getScore(String dutyJiChu, String dutyYiBan, String dutyZhongdian, String dutyMubiao, ScoreFlow flow1) {
        ScoreDetail detail = new ScoreDetail();
        JSONArray array = JSONArray.fromObject(dutyJiChu);
        List jichu = JSONArray.toList(array, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, jichu);


        JSONArray array1 = JSONArray.fromObject(dutyYiBan);
        List yiban = JSONArray.toList(array1, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, yiban);

        JSONArray array2 = JSONArray.fromObject(dutyZhongdian);
        List zhongdian = JSONArray.toList(array2, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, zhongdian);

        JSONArray array3 = JSONArray.fromObject(dutyMubiao);
        List mubiao = JSONArray.toList(array3, new DutyCodeAndScore(), new JsonConfig());
        judgeAddOrUpdate(flow1, detail, mubiao);
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

    @RequestMapping(value = "/jisuanScore", produces = "application/json;charset=utf-8")
    public Object jisuanScore(HttpServletRequest req) {
        //获取当前登录用户的编号
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            Map<String, Object> data = new LinkedHashMap<>();
            try {
                String msg = "";
                ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
                if(setTime != null) {
                    String year = setTime.getYear();
                    String month = setTime.getMonth();
                    List<MonthSummary> monthSummaryList = monthSummaryService.selectListByYearAndMonth(year, month);
                    if(monthSummaryList != null){
                        long count = monthSummaryList.stream().filter(s-> !s.getState().equals("7")).count();
                        if(count == 0){
                            List<ScoreHistory> scoreHistoryList = historyService.findUserScoreHistory(year, month);
                            if(scoreHistoryList.size() > 0){
                                List<UserScoreDto> userScoreDtoList = userScoreDtoService.findUserScore(year, month);
                                if(userScoreDtoList.size()>0){
//                                    Map<String, List<UserScoreDto>> byAuthor = userScoreDtoList.stream().map()
//                                            .collect(Collectors.groupingBy(UserScoreDto::getScoreType));
                                    List<String> typeList = new ArrayList<>();
                                    typeList.add("A");typeList.add("B");typeList.add("C");typeList.add("D");
                                    List<UserScoreDto> userQueryList = new ArrayList<>();
                                    Double totalRatio = 0.0;
                                    Double totalZdScore = 0.0;
                                    Double totalMbScore = 0.0;
                                    Double sumZdScore = 0.0;
                                    Double sumMbScore = 0.0;
                                    Double ratio = 0.0;
                                    Double score2 = 0.0;
                                    Double score3 = 0.0;
                                    Double ratioA = 0.0;
                                    Double score2A = 0.0;
                                    Double score3A = 0.0;
                                    Double ratioB = 0.0;
                                    Double score2B = 0.0;
                                    Double score3B = 0.0;
                                    Double ratioC = 0.0;
                                    Double score2C = 0.0;
                                    Double score3C = 0.0;
                                    Double ratioD = 0.0;
                                    Double score2D = 0.0;
                                    Double score3D = 0.0;
                                    long pingfenCount = 0;
//                                    long yZdCount = 0;
//                                    long nZdCount = 0;
//                                    long yMbCount = 0;
//                                    long nMbCount = 0;
                                    for (ScoreHistory item: scoreHistoryList){
                                        totalRatio = 0.0;
                                        userQueryList = userScoreDtoList.stream().filter(
                                                s->s.getScoredCode().equals(item.getUsercode())
                                        ).collect(Collectors.toList());

                                        for (String type : typeList) {
                                            pingfenCount = userQueryList.stream().filter(
                                                    s-> s.getScoreType().equals(type) && s.getDutyType().equals("2"))
                                                    .map(UserScoreDto::getScorringCode).distinct().count();

                                            score2 = userQueryList.stream().filter(
                                                    s-> s.getScoreType().equals(type) && s.getDutyType().equals("2")).mapToDouble(UserScoreDto::getScore).sum();

                                            score3 = userQueryList.stream().filter(
                                                    s-> s.getScoreType().equals(type) && s.getDutyType().equals("3")).mapToDouble(UserScoreDto::getScore).sum();

                                            ratio = userQueryList.stream().filter(s-> s.getScoreType().equals(type)).map(
                                                    UserScoreDto::getRatio).findFirst().orElse( 0.0 );

                                            score2 = score2.isNaN() ? 0 : score2;
                                            score3 = score3.isNaN() ? 0 : score3;

                                            totalRatio += ratio;
                                            score2 = score2 == 0 ? 0 : score2 / pingfenCount;
                                            score3 = score3 == 0 ? 0 : score3 / pingfenCount;
                                            if(type.equals("A")){
                                                ratioA = ratio;
                                                score2A = score2;
                                                score3A = score3;
                                            } else if(type.equals("B")){
                                                ratioB = ratio;
                                                score2B = score2;
                                                score3B = score3;
                                            } else if(type.equals("C")){
                                                ratioC = ratio;
                                                score2C = score2;
                                                score3C = score3;
                                            } else {
                                                ratioD = ratio;
                                                score2D = score2;
                                                score3D = score3;
                                            }
                                        }

                                        sumZdScore = score2A * ratioA + score2B * ratioB +
                                                score2C * ratioC + score2D * ratioD;

                                        sumMbScore = score3A * ratioA + score3B * ratioB +
                                                score3C * ratioC + score3D * ratioD;

                                        sumZdScore = sumZdScore == 0 ? 0 : sumZdScore / totalRatio;
                                        sumMbScore = sumMbScore == 0 ? 0 : sumMbScore / totalRatio;

                                        item.setSumZdScore(sumZdScore);
                                        item.setSumMbScore(sumMbScore);

//                                        if (sumZdScore == 0) {
//                                            nZdCount += 1;
//                                        } else {
//                                            yZdCount += 1;
//                                        }
//
//                                        if (sumMbScore == 0) {
//                                            nMbCount += 1;
//                                        } else {
//                                            yMbCount += 1;
//                                        }

                                        totalZdScore += sumZdScore;
                                        totalMbScore += sumMbScore;
                                    }

                                    totalZdScore = totalZdScore == 0 ? 0 : totalZdScore / scoreHistoryList.size();

                                    totalMbScore = totalMbScore == 0 ? 0 : totalMbScore / scoreHistoryList.size();

                                    for (ScoreHistory item: scoreHistoryList) {
                                        ScoreHistory scoreHistory = new ScoreHistory();
                                        scoreHistory.setId(item.getId());
                                        scoreHistory.setSumZdScore(item.getSumZdScore());
                                        scoreHistory.setSumMbScore(item.getSumMbScore());
                                        if(item.getSumZdScore() == 0) {
                                            scoreHistory.setAvgZdScore(totalZdScore);
                                        }else{
                                            scoreHistory.setAvgZdScore(0.00);
                                        }
                                        if(item.getSumMbScore() == 0) {
                                            scoreHistory.setAvgMbScore(totalMbScore);
                                        }else{
                                            scoreHistory.setAvgMbScore(0.00);
                                        }
                                        historyService.updateByPrimaryKeySelective(scoreHistory);
                                    }
                                    msg = "OK";
                                }else{
                                    msg = "考核月明细数据为空";
                                }
                            }else {
                                msg = "考核月历史数据为空";
                            }
                        } else {
                            msg = "考核月结数据还有评分未完成";
                        }
                    }else{
                        msg = "考核月结数据为空";
                    }
                } else {
                    msg =  "考核月为空";
                }
                map.put("msg", msg);
                map.put("code", msg.equals("OK") ? 0 : 1);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询个人考核详情失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

}
