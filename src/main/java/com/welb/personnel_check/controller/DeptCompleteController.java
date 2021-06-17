package com.welb.personnel_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.personnel_check.entity.*;
import com.welb.personnel_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DeptCompleteController
 * @projectName xh-360appraisal-interface
 * @description: 部门完成情况控制层
 * @date 2019/8/2315:40
 */
@RestController
@RequestMapping("/deptComplete")
public class DeptCompleteController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IDeptCompleteService deptCompleteService;
    @Resource
    IRaterService raterService;
    @Resource
    IAttachmentService attachmentService;
    @Resource
    IDeptCheckService deptCheckService;
    @Resource
    IPersonnelScorringService personnelScorringService;

    /**
     * 查询所有的部门完成情况数据（包含多条件查询） 默认查询当前季度的数据
     *
     * @param deptComplete
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Object selectAllDeptComplete(HttpServletRequest req, DeptComplete deptComplete, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                String year = CalendarUtil.getYear();
                String month = CalendarUtil.getMonth();
                String quarter = CalendarUtil.getQuarter(month);
                int count = Integer.parseInt(quarter) - 1;
                if (deptComplete.getYear().equals("") && deptComplete.getMonth().equals("")) {
                    if (count == 0) {
                        int lastyear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastyear);
                        month = "4";
                        getAllDeptComplete(deptComplete, pageNum, pageSize, map, year, month);
                    } else {
                        month = String.valueOf(count);
                        getAllDeptComplete(deptComplete, pageNum, pageSize, map, year, month);
                    }
                } else if (!deptComplete.getYear().equals("") && !deptComplete.getMonth().equals("")) {
                    int deptYear = Integer.parseInt(deptComplete.getYear());
                    int deptMonth = Integer.parseInt(deptComplete.getMonth());
                    if (count == 0) {
                        int lastyear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastyear);
                        month = "4";
                    } else {
                        month = String.valueOf(count);
                    }
                    int sysYear = Integer.parseInt(year);
                    int sysMonth = Integer.parseInt(month);
                    if (deptYear>sysYear||deptYear<2019) {
                        map.put("msg", "数据为空");
                        map.put("data", "");
                        map.put("code", 0);
                    }else {
                        if (deptYear==sysYear&&deptMonth>sysMonth){
                            map.put("msg", "数据为空");
                            map.put("data", "");
                            map.put("code", 0);
                        }else if (deptYear<=2019&&deptMonth<4){
                            map.put("msg", "数据为空");
                            map.put("data", "");
                            map.put("code", 0);
                        }
                        else {
                            getCompleteInfo(deptComplete.getYear(), deptComplete.getMonth());
                            PageHelper.startPage(pageNum, pageSize);
                            getDeptListInfo(deptComplete, map);
                        }
                    }
                } else if (!deptComplete.getYear().equals("") && deptComplete.getMonth().equals("") ||
                        deptComplete.getYear().equals("") && !deptComplete.getMonth().equals("")) {
                    map.put("msg", "年份和季度必须一起选择才可以搜索");
                    map.put("code", 1);
                }

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户过期,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getAllDeptComplete(DeptComplete deptComplete, int pageNum, int pageSize, ModelMap map, String year, String month) {
        getCompleteInfo(year, month);
        PageHelper.startPage(pageNum, pageSize);
        List<DeptComplete> list = deptCompleteService.selectList(deptComplete);
        PageInfo<DeptComplete> pageInfo = new PageInfo<>(list);
        map.put("totalPages", pageInfo.getTotal());
        map.put("data", list);
        map.put("msg", "查询成功");
        map.put("code", 0);
    }
    private void getDeptListInfo(DeptComplete deptComplete, ModelMap map) {
        List<DeptComplete> list = deptCompleteService.selectList(deptComplete);
        PageInfo<DeptComplete> pageInfo = new PageInfo<>(list);
        list = pageInfo.getList();
        map.put("data", list);
        map.put("totalPages", pageInfo.getTotal());

        map.put("msg", "查询成功");
        map.put("code", 0);
    }

    private void getCompleteInfo(String year, String month) {
        List<Rater> raters = raterService.selectList();
        for (Rater rater : raters) {
            DeptComplete dept = new DeptComplete();
            dept.setMoneycard(rater.getScorringcode());
            dept.setDeptname(rater.getDepartment());
            dept.setMonth(month);
            dept.setYear(year);
            //季度pdf上传
//            Attachment attachment = attachmentService.selectAttachmentByDepart(year, month, dept.getDeptname());
//            if (attachment != null) {// 1-已完成 2-未完成
//                dept.setPdfcompletemonth(1);
//            } else {
//                dept.setPdfcompletemonth(2);
//            }
            //年度pdf上传
            DeptCheck deptCheck = deptCheckService.selectDeptCheckByYearAndDepart(year, dept.getDeptname());
            if (deptCheck != null) {
                dept.setPdfcompleteyear(1);
            } else {
                dept.setPdfcompleteyear(2);
            }
            List<PersonnelScorring> personnelScorrings = personnelScorringService.selectListByMonthAndYearAndDepartmentName(year, month, dept.getDeptname());
            if (personnelScorrings.size() > 0) {
                dept.setExcelcomplete(1);
            } else {
                dept.setExcelcomplete(2);
            }
            if (dept.getPdfcompleteyear() == 1 &&
//                    dept.getPdfcompletemonth() == 1 &&
                    dept.getExcelcomplete() == 1) {
                dept.setComplete(1);
            } else {
                dept.setComplete(2);
            }
            DeptComplete deptComplete = deptCompleteService.selectByDepart(dept);
            if (deptComplete == null) {
                deptCompleteService.insertSelective(dept);
            } else {
                dept.setCompletecode(deptComplete.getCompletecode());
                deptCompleteService.updateByPrimaryKeySelective(dept);
            }
        }
    }

    /**
     * 查询分数excel和负责人上传pdf未上传的集合
     * @param deptComplete
     * @return
     */
    @RequestMapping("/getNoImportExcelAndPdf")
    public Object getNoImportExcelAndPdf(DeptComplete deptComplete){
        ModelMap map=new ModelMap();
        try {
            List<DeptComplete> list = deptCompleteService.getNoImportExcelAndPdf(deptComplete);
            map.put("pageTotals",list.size());
            map.put("data", list);
            map.put("msg", "查询成功");
            map.put("code", 0);
        }catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("error",e.getMessage());
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }
}
