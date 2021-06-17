package com.welb.organization_check.controller;

import com.welb.organization_check.entity.AssessmentState;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.MonthSummary;
import com.welb.organization_check.service.IAssessmentStateService;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IMonthSummaryService;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: ManualSetTimeController
 * @projectName xh-360appraisal-interface
 * @description: 手动设置考核时间控制器
 * @date 2020/2/1614:44
 */
@RestController
@RequestMapping("/manualSetTime")
public class ManualSetTimeController {
    private final Logger log = LogManager.getLogger(this.getClass());
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IAssessmentStateService stateService;
    @Resource
    IMonthSummaryService monthSummaryService;

    /**
     *是否确认开启新的季度考核
     * @return
     */
    @RequestMapping("/isAllFinish")
    public Object isAllFinish() {
        ModelMap map = new ModelMap();
        String year = CalendarUtil.getYear();
        String month = CalendarUtil.getMonth();
        String quarter = CalendarUtil.getQuarter(month);
        int count = Integer.parseInt(quarter) - 1;
        if (count == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "4";
        } else {
            month = String.valueOf(count);
        }
        List<MonthSummary> list = monthSummaryService.selectListByYearAndMonth(year, month);
        boolean isFinish = true;
        for (MonthSummary summary : list) {
            if (!summary.getState().equals("7")) {
                isFinish = false;
                break;
            }

        }
        if (isFinish){
            map.put("msg","是否确认开启新的季度考核新的考核");
            map.put("code",0);
        }else {
            map.put("msg","当前季度考核还未全部完成,是否结束当前季度考核并开始新的季度考核。");
            map.put("code",0);
        }
        return map;
    }

    /**
     * 开启手动考核按钮接口
     *
     * @return
     */
    @RequestMapping("/openManualAssessment")
    public Object openManualAssessment(ManualSetTime setTime) {
        ModelMap map = new ModelMap();
        try {
            String year = CalendarUtil.getYear();
            String month = CalendarUtil.getMonth();
            String quarter = CalendarUtil.getQuarter(month);
            setTime.setYear(year);
            setTime.setMonth(quarter);
            String time = DateUtil.getTime();
            setTime.setCreatetime(time);
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, quarter);
            if (manualSetTime!=null){
                //修改
                setTime.setId(manualSetTime.getId());
                setTimeService.updateByPrimaryKeySelective(setTime);
            }else {
               //添加
                setTimeService.insertSelective(setTime);
            }
            //修改手动考核按钮状态
            updateState();
            map.put("msg", "手动考核开启成功，请重新登陆系统进行操作");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;

    }

    private void updateState() {
        AssessmentState assessmentState = new AssessmentState();
        assessmentState.setId(1);
        assessmentState.setState("1");
        stateService.updateByPrimaryKeySelective(assessmentState);
    }

    /**
     * 修改手动考核设置时间
     *
     * @return
     */
    @RequestMapping("/updateManualAssessment")
    public Object updateManualAssessment(ManualSetTime setTime) {
        ModelMap map = new ModelMap();
        try {
            String year = CalendarUtil.getYear();
            String month = CalendarUtil.getMonth();
            String quarter = CalendarUtil.getQuarter(month);
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, quarter);
            if (manualSetTime == null) {
                int count = Integer.parseInt(quarter) - 1;
                if (count == 0) {
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                    month = "4";
                } else {
                    month = String.valueOf(count);
                }
                setTime.setYear(year);
                setTime.setMonth(month);
            }else {
                setTime.setYear(year);
                setTime.setMonth(quarter);
            }

            String time = DateUtil.getTime();
            setTime.setUpdatetime(time);
            setTimeService.updateTimeByYearAndMonth(setTime);
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;

    }

    /**
     * 关闭手动考核按钮接口
     *
     * @return
     */
    @RequestMapping("/closeManualAssessment")
    public Object closeManualAssessment(AssessmentState assessmentState) {
        ModelMap map = new ModelMap();
        try {
            //修改手动考核按钮状态
            assessmentState.setState("2");
            stateService.updateByPrimaryKeySelective(assessmentState);
            map.put("msg", "自动考核开启成功，请重新登陆系统进行操作");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;

    }

}