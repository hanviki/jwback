package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.ManualSetTime;
import com.welb.organization_check.entity.MonthSummary;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IManualSetTimeService;
import com.welb.organization_check.service.IMonthSummaryService;
import com.welb.organization_check.service.IUserService;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author luoyaozu
 * @title: MonthSummaryController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/5/300:00
 */
@RestController
@RequestMapping("/monthsummary")
public class MonthSummaryController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    IMonthSummaryService summaryService;
    @Resource
    IUserService userService;
    @Resource
    IManualSetTimeService setTimeService;

    /**
     * 查询个人季节总结 包含模糊匹配查询
     *
     * @param summary
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectSummary(MonthSummary summary, int pageNum, int pageSize, HttpServletRequest req) throws ParseException {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
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
                //手动考核 --查询个人季节总结
                manualSelectSummary(summary, pageNum, pageSize, map, usercode, year, month, count, sysTime);

            } else {
                //自动考核 --查询个人季节总结
                automaticSelectSummary(summary, pageNum, pageSize, map, usercode, year, count);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectSummary(MonthSummary summary, int pageNum, int pageSize, ModelMap map, String usercode, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            //开始新的一轮考核 手动设置考核时间未超过自动考核时间
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的考核
                month = quarter;
                getSummaryList(summary, pageNum, pageSize, map, usercode, year, month);
            } else {
                //还没有到指定考核时间仍展示上一季度数据
                automaticSelectSummary(summary, pageNum, pageSize, map, usercode, year, count);
            }

        } else {
            //开始新的一轮考核 手动设置考核时间超过自动考核时间
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                getSummaryList(summary, pageNum, pageSize, map, usercode, year, month);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                getSummaryList(summary, pageNum, pageSize, map, usercode, year, month);
            }

        }
    }

    private void getSummaryList(MonthSummary summary, int pageNum, int pageSize, ModelMap map, String usercode, String year, String month) {
        map.put("year", year);
        map.put("month", month);
        String serialno = year + "-" + month + "-" + usercode;
        MonthSummary summary1 = summaryService.selectByPrimaryKey(serialno);
        getSummarys(summary, map, usercode, summary1, pageNum, pageSize);
    }

    private void automaticSelectSummary(MonthSummary summary, int pageNum, int pageSize, ModelMap map, String usercode, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";

        } else {
            month = String.valueOf(count);
        }
        getSummaryList(summary, pageNum, pageSize, map, usercode, year, month);
    }

    private void getSummarys(MonthSummary summary, ModelMap map, String usercode, MonthSummary summary1, int pageNum, int pageSize) {
        String isAdd;//0:未添加  1：已经添加
        if (summary1 != null) {
            if (summary1.getState().equals("0")) {
                isAdd = "0";
            } else {
                isAdd = "1";
            }
            map.put("isAdd", isAdd);
            summary.setEmployeecode(usercode);
            getSummaryLists(summary, map, usercode, pageNum, pageSize);
        } else {
            isAdd = "0";
            map.put("isAdd", isAdd);
            summary.setEmployeecode(usercode);
            getSummaryLists(summary, map, usercode, pageNum, pageSize);
        }
    }

    private void getSummaryLists(MonthSummary summary, ModelMap map, String usercode, int pageNum, int pageSize) {
        List<MonthSummary> summaryList;
        try {
            PageHelper.startPage(pageNum, pageSize);
            summaryList = summaryService.selectSummaryLikeAll(summary);
            User user = userService.findUserByUserCode(usercode);
            String username = user.getUsername();
            if (user == null) {
                map.put("employeename", "");
            } else {
                map.put("employeename", username);
            }
            PageInfo<MonthSummary> pageInfo = new PageInfo<>(summaryList);
            summaryList = pageInfo.getList();
            //数据总量
            map.put("totalPages", pageInfo.getTotal());
            map.put("data", summaryList);
            map.put("msg", "查询个人季节总结成功 ");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询个人季节总结失败 ");
            map.put("code", 1);
        }
    }


    /**
     * 添加个人季节总结
     *
     * @param summary
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object addMonthSummary(HttpServletRequest req, MonthSummary summary) throws ParseException {
        ModelMap map = new ModelMap();
        String time = DateUtil.getTime();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        //字符串转换成日期格式
        summary.setPubdate(date);
        //获取当前年份
        String year = CalendarUtil.getYear();
        //获取当前月份
        String month = CalendarUtil.getMonth();
        //获取当前季度
       // String quarter = CalendarUtil.getQuarter(month);
        //当前上一个季度
        int count = Integer.parseInt(month.trim()) - 1;
        String sysTime = DateUtil.getTime();
        if (state.equals("1")) {
            //手动考核 --添加个人季节总结
            manualAddSummary(summary, map, usercode, year, month, count, sysTime);

        } else {
            //自动考核  --添加个人季节总结
            automaticAddSummary(summary, map, usercode, year, count);
        }
        return map;
    }

    private void manualAddSummary(MonthSummary summary, ModelMap map, String usercode, String year, String quarter, int count, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        //开始新的一轮考核 手动设置考核时间未超过自动考核时间
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //开始新的考核
                month = quarter;
                summary.setMonth(month);
                summary.setYear(year);
                //拼接个人季节唯一标识
                String serialno = summary.getYear() + "-" + summary.getMonth() + "-" + usercode;
                MonthSummary summary1 = summaryService.selectByPrimaryKey(serialno);
                addOrUpdateSummary(summary, map, usercode, serialno, summary1);
            } else {
                //还没有到指定考核时间仍展示上一季度数据
                automaticAddSummary(summary, map, usercode, year, count);

            }
        } else {
            //开始新的一轮考核 手动设置考核时间超过自动考核时间
            if (count == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "12";
            } else {
                month = String.valueOf(count);
            }
            ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth(year, month);
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(manualSetTime.getTime()).getTime()) {
                //拼接个人季节唯一标识
                String serialno = summary.getYear() + "-" + summary.getMonth() + "-" + usercode;
                MonthSummary summary1 = summaryService.selectByPrimaryKey(serialno);
                addOrUpdateSummary(summary, map, usercode, serialno, summary1);
            } else {
                int lastMonth = Integer.parseInt(month) - 1;
                if (lastMonth == 0) {
                    lastMonth = 12;
                    int lastYear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastYear);
                }
                month = String.valueOf(lastMonth);
                summary.setYear(year);
                summary.setMonth(month);
                //拼接个人季节唯一标识
                String serialno = summary.getYear() + "-" + summary.getMonth() + "-" + usercode;
                MonthSummary summary1 = summaryService.selectByPrimaryKey(serialno);
                addOrUpdateSummary(summary, map, usercode, serialno, summary1);
            }
        }
    }

    private void addOrUpdateSummary(MonthSummary summary, ModelMap map, String usercode, String serialno, MonthSummary summary1) {
        if (summary1 != null) {
            getSummaryInfo(summary, map, summary1);
        } else {
            summary.setSerialno(serialno);
            summary.setEmployeecode(usercode);
            if (summary.getContent().length() > 10000) {
                map.put("msg", "最多只能上传一万个字数,请使用附件上传");
                map.put("code", 1);
            } else {
                int count1 = summaryService.insertSelective(summary);
                getCount(map, count1);
                //更新状态
                summary.setState("1");
                summaryService.updateByPrimaryKeySelective(summary);
            }
        }
    }

    private void automaticAddSummary(MonthSummary summary, ModelMap map, String usercode, String year, int count) {
        String month;
        if (count == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            summary.setYear(String.valueOf(lastyear));
            summary.setMonth("12");
        } else {
            month = String.valueOf(count);
            summary.setMonth(month);
            summary.setYear(year);
        }
        //拼接个人季节唯一标识
        String serialno = summary.getYear() + "-" + summary.getMonth() + "-" + usercode;
        MonthSummary summary1 = summaryService.selectByPrimaryKey(serialno);
        addOrUpdateSummary(summary, map, usercode, serialno, summary1);
    }

    private void getSummaryInfo(MonthSummary summary, ModelMap map, MonthSummary summary1) {
        if (summary1.getState().equals("0")) {
            summary1.setState("1");
        }
        summary1.setTitle(summary.getTitle());
        summary1.setContent(summary.getContent());
        summary1.setSavepath(summary.getSavepath());
        summary1.setFilename(summary.getFilename());
        int count1 = summaryService.updateByPrimaryKeySelective(summary1);
        getCount(map, count1);
    }

    private void getCount(ModelMap map, int count1) {
        if (count1 > 0) {
            map.put("msg", "添加个人季节总结成功");
            map.put("code", 0);
        } else {
            map.put("msg", "添加个人季节总结失败");
            map.put("code", 1);
        }
    }

    /**
     * 修改个人季节总结
     *
     * @param summary
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateMonthSummary(MonthSummary summary) {
        ModelMap map = new ModelMap();
        String time = DateUtil.getTime();
        Date date = null;
        MonthSummary summary1 = summaryService.selectByPrimaryKey(summary.getSerialno());
        summary.setState(summary1.getState());
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ;//字符串转换成日期格式
        summary.setPubdate(date);
        if (summary.getContent().length() > 10000) {
            map.put("msg", "最多只能上传一万个字数,请使用附件上传");
            map.put("code", 1);
        } else {
            int count = summaryService.updateByPrimaryKeySelective(summary);
            if (count > 0) {
                map.put("msg", "修改个人季节总结成功");
                map.put("code", 0);
            } else {
                map.put("msg", "修改个人季节总结失败");
                map.put("code", 1);
            }
        }
        return map;
    }


    @RequestMapping("/getHistoryContent")
    public Object getHistoryContent() {
        ModelMap map = new ModelMap();
        StringBuilder sb = new StringBuilder();
        try {
            List<MonthSummary> summaryList = summaryService.findMonthSummaryAll();
            for (MonthSummary summary : summaryList) {
                if (summary.getFormattext() != null) {
                    if (summary.getContent() == null || summary.getContent().equals("")) {
                        byte[] formattext = summary.getFormattext();
                        //将字节数组转换成inputstream流
                        InputStream sbs = new ByteArrayInputStream(formattext);
                        //inputstream流转换成blob
                        ByteArrayOutputStream result = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = sbs.read(buffer)) != -1) {
                            result.write(buffer, 0, length);
                        }
                        //转换成字符串
                        String content = result.toString(StandardCharsets.UTF_8.name());
                        if (content.length() > 10000) {
                            sb.append(summary.getSerialno()).append(";");
                        } else {
                            summary.setContent(content);
                            summaryService.updateByPrimaryKeySelective(summary);
                        }
                    }
                }
            }
            map.put("sb", sb);
            map.put("msg", "获取内容成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "获取内容失败");
            map.put("code", 1);
        }
        return map;
    }


    @RequestMapping("/getOneHistoryContent")
    public String getOneHistoryContent(String serialno) throws IOException {

        MonthSummary summary = summaryService.selectByPrimaryKey(serialno);
        byte[] formattext = summary.getFormattext();
        //将字节数组转换成inputstream流
        InputStream sbs = new ByteArrayInputStream(formattext);
        //inputstream流转换成blob
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = sbs.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        //转换成字符串
        String content = result.toString(StandardCharsets.UTF_8.name());
        return content;

    }
}
