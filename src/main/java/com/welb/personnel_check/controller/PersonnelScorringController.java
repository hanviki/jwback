package com.welb.personnel_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.personnel_check.entity.*;
import com.welb.personnel_check.info.PersonnelInfo;
import com.welb.personnel_check.info.PersonnelScorringInfo;
import com.welb.personnel_check.info.PersonnelScorringInfoLike;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.personnel_check.service.*;
import com.welb.util.*;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * @author luoyaozu
 * @title: PersonnelScorringController
 * @projectName xh-360appraisal-interface
 * @description: 人事评分控制层
 * @date 2019/8/1211:24
 */
@RestController
@RequestMapping("/personnel")
public class PersonnelScorringController {
    private final Logger log = LogManager.getLogger(this.getClass());
    @Resource
    IPersonnelScorringService personnelScorringService;
    @Resource
    IAttachmentService attachmentService;
    @Resource
    IUserService userService;
    @Resource
    IPersonnelUserService personnelUserService;
    @Resource
    IRaterService raterService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IExcelLogService excelLogService;

    /**
     * 模糊查询所有的认识评分数据 包含多条件查询
     * * @param req
     *
     * @param personnel
     * @return
     */
    @RequestMapping("/list")
    public Object selectAllLike(HttpServletRequest req, PersonnelScorring personnel, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        List<PersonnelScorring> personnelScorrings;
        if (usercode != null) {
            User user = userService.findRaterUserByUserCode(usercode);
            Rater rater = raterService.selectByMoneyCard(user.getMoneycard());
            boolean flag = false;
            List<UserRoleKey> role = userRoleService.selectUserRoleByUserCode(usercode);
            try {
                String sysYear = CalendarUtil.getYear();
                String sysMonth = CalendarUtil.getMonth();
                String quarter = CalendarUtil.getQuarter(sysMonth);
                int count = Integer.parseInt(quarter) - 1;
                if (count == 0) {
                    int parseInt = Integer.parseInt(sysYear) - 1;
                    sysYear = String.valueOf(parseInt);
                    sysMonth = "4";
                } else {
                    sysMonth = String.valueOf(count);
                }
                if (personnel.getYear() == null && personnel.getMonth() == null
                        || personnel.getYear().equals("") && personnel.getMonth().equals("")) {
                    personnel.setYear(sysYear);
                    personnel.setMonth(sysMonth);
                }
                if (rater != null) {
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400") && !rater.getDepartment().equals("人事处")) {
                            flag = true;
                        }
                    }
                } else {
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400")) {
                            flag = true;
                        }
                    }
                }
                PageHelper.startPage(pageNum, pageSize);
                if (flag == true) {//超级管理员
                    personnelScorrings = personnelScorringService.selectAllLikeByManager(personnel);
                } else {//部门长
                    personnel.setDepartmentname(rater.getDepartment());
                    personnelScorrings = personnelScorringService.selectAllLikeByManager(personnel);
                }
                if (personnelScorrings.size() > 0) {
                    PageInfo<PersonnelScorring> pageInfo = new PageInfo<>(personnelScorrings);
                    personnelScorrings = pageInfo.getList();
                    //获取上传pdf信息
                    getAttachmentInfo(personnel.getYear(), personnel.getMonth(), map, rater, user.getMoneycard());
                    map.put("pageTotals", pageInfo.getTotal());
                    map.put("data", personnelScorrings);
                    map.put("msg", "查询成功");
                    map.put("code", 0);
                } else {
                    //获取上传pdf信息
                    getAttachmentInfo(personnel.getYear(), personnel.getMonth(), map, rater, user.getMoneycard());
                    map.put("msg", "暂无数据");
                    map.put("code", 0);
                }
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户过期,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    private void getAttachmentInfo(String year, String month, ModelMap map, Rater rater, String moneycard) {
        Attachment attachment;
        if (year != null && month != null || !year.equals("") && !month.equals("")) {
            if (rater != null) {
                attachment = attachmentService.selectAttachmentByDepart(year, month, rater.getDepartment());

            } else {
                attachment = attachmentService.selectAttachmentByYearAndMonth(year, month, moneycard);
            }
            if (attachment == null) {
                map.put("attachment", "");
            } else {
                map.put("attachment", attachment);
            }
        }
    }

    /**
     * 下载模板
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/downloadTemplate", produces = "application/json;charset=utf-8")
    public Object downloadTemplate(HttpServletResponse response, String moneycard) {
        ModelMap map = new ModelMap();

        // 定义表的标题
        String title = "评分列表";
        //定义表的列名
        String[] rowsName = new String[]{"序号", "发薪号", "用户姓名", "所属科室", "分数（分值为1-100）", "备注"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<>();
        Rater rater = raterService.selectByMoneyCard(moneycard);
        if (rater != null) {//人事部用户管理存在该用户
            List<PersonnelUser> list = personnelUserService.selectPersonnelByDepartmentName(rater.getDepartment());
            if (list.size() > 0) {
                //定义表的内容
                Object[] objs;
                for (int i = 0; i < list.size(); i++) {
                    objs = new Object[rowsName.length];
                    objs[0] = i;
                    objs[1] = list.get(i).getMoneycard();
                    objs[2] = list.get(i).getUsername();
                    objs[3] = list.get(i).getDepartmentname();
                    objs[4] = "";
                    objs[5] = "";
                    dataList.add(objs);
                }
            }
            // 创建ExportExcel对象
            ExcelUtil excelUtil = new ExcelUtil();
            try {
                String text = rater.getDepartment() + "评分导入模板 .xls";
                String fileName = new String(text.getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
                excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
                map.put("msg", "下载成功");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "下载失败");
                map.put("code", 1);
            }
        } else {//人事部用户管理不存在该用户
            map.put("msg", "请先在用户管理中添加该用户的相关信息");
            map.put("code", 1);

        }
        return map;
    }

    /**
     * 非人事部管理员下载模板
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/downloadTemplateByManager", produces = "application/json;charset=utf-8")
    public Object downloadTemplateByManager(HttpServletResponse response, String departmentname) {
        ModelMap map = new ModelMap();
        // 定义表的标题
        String title = "评分列表";
        //定义表的列名
        String[] rowsName = new String[]{"序号", "发薪号", "用户姓名", "所属部门", "分数（分值为1-100）", "备注"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<>();
        List<PersonnelUser> list = personnelUserService.selectPersonnelByDepartmentName(departmentname);
        if (list.size() > 0) {
            //定义表的内容
            Object[] objs;
            for (int i = 0; i < list.size(); i++) {
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = list.get(i).getMoneycard();
                objs[2] = list.get(i).getUsername();
                objs[3] = list.get(i).getDepartmentname();
                objs[4] = "";
                objs[5] = "";
                dataList.add(objs);
            }

        } // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try {
            String text = departmentname + "评分导入模板 .xls";
            String fileName = new String(text.getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
            map.put("msg", "下载成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "下载失败");
            map.put("code", 1);
        }

        return map;
    }

    /**
     * 导入分数excel表
     *
     * @return
     * @throws Exception
     */
    @RequestMapping("/importExcel")
    public Object importExcel(HttpServletRequest req, @RequestParam("file") MultipartFile file) {
        ModelMap map = new ModelMap();
        try {
            //获取YYYYMMDD格式的日期
            String days = DateUtil.getDays();
            //获取yyyyMMddHHmmss格式的日期
            String times = DateUtil.getSdfTimes();
            //获取路径
            String realPath = req.getContextPath() + File.separator + "excels" + File.separator;
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
            String savepath = newfile.getPath().replaceAll("\\\\", "/");
            if (filename.contains(".xls") || filename.contains(".xlsx")) {
                map.put("savepath", savepath);
                map.put("msg", "上传成功");
                map.put("code", 0);
            } else {
                map.put("msg", "上传的文件格式不对,请先下载导出模板");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "上传失败,请核对上传分数模板是否正确");
            map.put("code", 1);

        }
        return map;
    }

    /**
     * 确认上传分数excel表
     *
     * @param year
     * @param month
     * @param usercode
     * @param savepath
     * @return
     */
    @RequestMapping("/confirmImportExcel")
    public Object confirmImportExcel(String year, String month, String usercode, String savepath) {
        ModelMap map = new ModelMap();
        int startRow = 3;
        int endRow = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            //添加excel表上传的行为日志
            ExcelLog excelLog = new ExcelLog();
            excelLog.setSavepath(savepath);
            excelLog.setOperatetime(format.format(new Date()));
            List<PersonnelScorring> personnelScorringList;
            User user = userService.findRaterUserByUserCode(usercode);
            Rater rater = raterService.selectByMoneyCard(user.getMoneycard());
            if (year.equals("") && month.equals("") || year.equals("null") && month.equals("null")) {//初始化默认的是当前年份的上一季度
                year = CalendarUtil.getYear();
                month = CalendarUtil.getMonth();
                String quarter = CalendarUtil.getQuarter(month);
                int count = Integer.parseInt(quarter) - 1;
                if (count == 0) {
                    int lastyear = Integer.parseInt(year) - 1;
                    year = String.valueOf(lastyear);
                    month = "4";
                    setExcellog(year, month, excelLog, rater.getScorringcode(), rater.getDepartment());
                    personnelScorringList = personnelScorringService.selectListByMonthAndYearAndDepartmentName(year, month, rater.getDepartment());
                    judgeImportExcel(rater, savepath, map, startRow, endRow, year, month, personnelScorringList,excelLog);
                } else {
                    month = String.valueOf(count);
                    setExcellog(year, month, excelLog, rater.getScorringcode(), rater.getDepartment());
                    personnelScorringList = personnelScorringService.selectListByMonthAndYearAndDepartmentName(year, month, rater.getDepartment());
                    judgeImportExcel(rater, savepath, map, startRow, endRow, year, month, personnelScorringList,excelLog);
                }
            } else if (year.equals("") && !month.equals("") || !year.equals("") && month.equals("")) {
                map.put("msg", "年份和季度必须一起选择");
                map.put("code", 1);
            } else if (year.equals("null") && !month.equals("null") || !year.equals("null") && month.equals("null")) {
                map.put("msg", "年份和季度必须一起选择");
                map.put("code", 1);
            } else {
                setExcellog(year, month, excelLog, rater.getScorringcode(), rater.getDepartment());
                personnelScorringList = personnelScorringService.selectListByMonthAndYearAndDepartmentName(year, month, rater.getDepartment());
                judgeImportExcel(rater, savepath, map, startRow, endRow, year, month, personnelScorringList,excelLog);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "上传失败,请核对上传分数模板是否正确");
            map.put("code", 1);
        }
        return map;
    }

    private void setExcellog(String year, String month, ExcelLog excelLog, String moneycard, String departmentname) {
        excelLog.setYear(year);
        excelLog.setMonth(month);
        excelLog.setMoneycard(moneycard);
        excelLog.setDepartmentname(departmentname);
    }

    /**
     * 非人事部管理员确认上传分数excel表
     *
     * @param year
     * @param month
     * @param savepath
     * @param departmentname
     * @return
     */
    @RequestMapping("/confirmImportExcelByManager")
    public Object confirmImportExcelByManager(HttpServletRequest req,String year, String month, String savepath, String departmentname) {
        ModelMap map = new ModelMap();
        int startRow = 3;
        int endRow = 0;

        if (departmentname.equals("")) {
            map.put("msg", "请先选择模板部门，再上传分数excel表");
            map.put("code", 1);
        } else {
            try {
                String  usercode = (String) req.getSession().getAttribute("usercode");
                User user = userService.findUserByUserCode(usercode);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //添加excel表上传的行为日志
                ExcelLog excelLog = new ExcelLog();
                excelLog.setSavepath(savepath);
                excelLog.setOperatetime(format.format(new Date()));
                List<PersonnelScorring> personnelScorringList;
                if (year.equals("") && month.equals("") || year.equals("null") && month.equals("null")) {//初始化默认的是当前年份的上一季度
                    year = CalendarUtil.getYear();
                    month = CalendarUtil.getMonth();
                    String quarter = CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(quarter) - 1;
                    if (count == 0) {
                        int lastyear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastyear);
                        month = "4";
                        setExcellog(year, month, excelLog, user.getMoneycard(), departmentname);
                        personnelScorringList = personnelScorringService.selectListByMonthAndYearAndDepartmentName(year, month, departmentname);
                        judgeImportExcelByManager(departmentname, savepath, map, startRow, endRow, year, month, personnelScorringList,excelLog);
                    } else {
                        month = String.valueOf(count);
                        setExcellog(year, month, excelLog, user.getMoneycard(), departmentname);
                        personnelScorringList = personnelScorringService.selectListByMonthAndYearAndDepartmentName(year, month, departmentname);
                        judgeImportExcelByManager(departmentname, savepath, map, startRow, endRow, year, month, personnelScorringList,excelLog);
                    }
                } else if (year.equals("") && !month.equals("") || !year.equals("") && month.equals("")) {
                    map.put("msg", "年份和季度必须一起选择");
                    map.put("code", 1);
                } else if (year.equals("null") && !month.equals("null") || !year.equals("null") && month.equals("null")) {
                    map.put("msg", "年份和季度必须一起选择");
                    map.put("code", 1);
                } else {
                    setExcellog(year, month, excelLog, user.getMoneycard(), departmentname);
                    personnelScorringList = personnelScorringService.selectListByMonthAndYearAndDepartmentName(year, month, departmentname);
                    judgeImportExcelByManager(departmentname, savepath, map, startRow, endRow, year, month, personnelScorringList,excelLog);
                }
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "上传失败,请核对上传分数模板是否正确");
                map.put("code", 1);
            }
        }
        return map;
    }

    //部门长
    private void judgeImportExcel(Rater rater, String savepath, ModelMap map, int startRow, int endRow, String year, String month, List<PersonnelScorring> personnelScorringList,ExcelLog excelLog) throws IOException {
        //各部门长录入的人员信息
        List<PersonnelScorringInfo> personnelScorrings = (List<PersonnelScorringInfo>) ImportExcel.importExcel(savepath, startRow, endRow, PersonnelScorringInfo.class);
        List<PersonnelScorringInfo> personnelScorringsNew = (List<PersonnelScorringInfo>) ImportExcel.importExcel(savepath, startRow, endRow, PersonnelScorringInfo.class);
        //判断部门长导入的数据是否跟人事部用户数据匹配
        judgeImportExcelInfoIsCorrect(rater, map, year, month, personnelScorrings, personnelScorringsNew, personnelScorringList,excelLog);
    }

    //非人事部管理员
    private void judgeImportExcelByManager(String departmentname, String savepath, ModelMap map, int startRow, int endRow, String year, String month,
                                           List<PersonnelScorring> personnelScorringList,ExcelLog excelLog) throws IOException {
        //各部门长录入的人员信息
        List<PersonnelScorringInfo> personnelScorrings = (List<PersonnelScorringInfo>) ImportExcel.importExcel(savepath, startRow, endRow, PersonnelScorringInfo.class);
        List<PersonnelScorringInfo> personnelScorringsNew = (List<PersonnelScorringInfo>) ImportExcel.importExcel(savepath, startRow, endRow, PersonnelScorringInfo.class);
        //判断部门长导入的数据是否跟人事部用户数据匹配
        judgeImportExcelInfoIsCorrectByManager(departmentname, map, year, month, personnelScorrings, personnelScorringsNew, personnelScorringList,excelLog);
    }

    //部门长
    private void judgeImportExcelInfoIsCorrect(Rater rater, ModelMap map, String year, String month, List<PersonnelScorringInfo> personnelScorrings,
                                               List<PersonnelScorringInfo> personnelScorringsNew, List<PersonnelScorring> personnelScorringList,ExcelLog excelLog) {
        List<PersonnelUser> personnelUsers;
        //通过组织部长所在的部门查找人事部录入的所有人员信息

        personnelUsers = personnelUserService.selectListByDeptName(rater.getDepartment());

        getPersonnelScorrings(map, year, month, personnelScorrings, personnelScorringsNew, personnelScorringList, personnelUsers,excelLog);
    }

    //非人事部管理员权限
    private void judgeImportExcelInfoIsCorrectByManager(String departmentname, ModelMap map, String year, String month, List<PersonnelScorringInfo> personnelScorrings,
                                                        List<PersonnelScorringInfo> personnelScorringsNew, List<PersonnelScorring> personnelScorringList,ExcelLog excelLog) {
        List<PersonnelUser> personnelUsers;
        //通过组织部长所在的部门查找人事部录入的所有人员信息

        personnelUsers = personnelUserService.selectListByDeptName(departmentname);

        getPersonnelScorrings(map, year, month, personnelScorrings, personnelScorringsNew, personnelScorringList, personnelUsers,excelLog);
    }

    private void getPersonnelScorrings(ModelMap map, String year, String month, List<PersonnelScorringInfo> personnelScorrings, List<PersonnelScorringInfo> personnelScorringsNew,
                                       List<PersonnelScorring> personnelScorringList, List<PersonnelUser> personnelUsers,ExcelLog excelLog) {
        if (personnelScorrings.size() > personnelUsers.size()) {
            //获取多上传的相关人员信息
            getMoreUploadInfo(map, personnelScorrings, personnelUsers,year,month);
        } else if (personnelScorrings.size() < personnelUsers.size()) {
            //获取没有上传的相关人员信息
            getLessUploadInfo(map, personnelScorrings, personnelUsers);
        } else if (personnelScorrings.size() == personnelUsers.size()) {
            getSameUploadInfo(map, year, month, personnelScorrings, personnelScorringsNew, personnelScorringList, personnelUsers,excelLog);
        } else {
            //获取信息错误的人员相关信息
            getErrorInfo(map, personnelScorrings);
        }
    }

    private void getSameUploadInfo(ModelMap map, String year, String month, List<PersonnelScorringInfo> personnelScorrings, List<PersonnelScorringInfo> personnelScorringsNew,
                                   List<PersonnelScorring> personnelScorringList, List<PersonnelUser> personnelUsers,ExcelLog excelLog) {
        List<PersonnelScorringInfo> scorrings = new ArrayList<>();
        List<String> moneycards = new ArrayList<>();
        for (PersonnelScorringInfo info : personnelScorrings) {
            moneycards.add(info.getMoneycard());
        }
        Set<String> set = new HashSet<>();
        Set<String> setMore = new HashSet<>();
        for (String ls : moneycards) {
            if (set.contains(ls)) {
                setMore.add(ls);
            }
            set.add(ls);
        }
        if (setMore.size() > 0) {//判断是否有重复数据
            getRepeatData(map, setMore);
        } else {
            List<PersonnelScorringInfo> users = new ArrayList();
            List<PersonnelScorringInfo> infos = new ArrayList();//存的是分数异常（分数<0或者>100）集合
            for (int i = 0; i < personnelUsers.size(); i++) {
                for (int j = 0; j < personnelScorrings.size(); j++) {
                    String monryCard = personnelScorrings.get(j).getMoneycard().replaceAll(" ", "");
//                    String userName=personnelScorrings.get(j).getUsername().replaceAll(" ","");
//                    String departmentName=personnelScorrings.get(j).getDepartmentname().replaceAll(" ","");
                    String score = personnelScorrings.get(j).getScore();
                    if (personnelUsers.get(i).getMoneycard().equals(monryCard)/*&&
                        personnelUsers.get(i).getUsername().equals(userName) &&
                        personnelUsers.get(i).getDepartmentname().equals(departmentName)*/) {
                        users.add(personnelScorrings.get(i));
                        if (!score.equals("")) {
                            double scoreNum = Double.parseDouble(score);
                            if (scoreNum > 100 || scoreNum < 0) {
                                infos.add(personnelScorrings.get(i));
                            }
                        }
                        break;
                    }
                }
            }
            if (infos.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < infos.size(); i++) {
                    if (i == infos.size() - 1) {
                        sb.append(infos.get(i).getUsername() + "(" + infos.get(i).getMoneycard() + "-" + infos.get(i).getDepartmentname() + ")");
                    } else {
                        sb.append(infos.get(i).getUsername() + "(" + infos.get(i).getMoneycard() + "-" + infos.get(i).getDepartmentname() + ")").append("\n");

                    }
                }
                map.put("size", infos.size());
                map.put("msg", sb + "的分数不在打分范围之内（0~100），请仔细核对并重新上传");
                map.put("code", 1);
            } else {
                personnelScorrings.removeAll(users);
                if (personnelScorrings.size() > 0) {
                    getErrorInfo(map, personnelScorrings);
                } else {
                    uploadExcelSuccess(map, year, month, personnelScorringsNew, personnelScorringList, personnelUsers, scorrings,excelLog);
                }
            }
        }
    }

    private void getMoreUploadInfo(ModelMap map, List<PersonnelScorringInfo> personnelScorrings, List<PersonnelUser> personnelUsers
                                   ,String year,String month) {
        //将导入数据中的发薪号一一遍历存放到moneycards集合中
        List<String> moneycards = new ArrayList<>();
        for (PersonnelScorringInfo info : personnelScorrings) {
            moneycards.add(info.getMoneycard());
        }
        Set<String> set = new HashSet<>();
        Set<String> setMore = new HashSet<>();
        for (String ls : moneycards) {
            if (set.contains(ls)) {
                setMore.add(ls);
            }
            set.add(ls);
        }
        if (setMore.size() > 0) {//判断是否有重复数据
            getRepeatData(map, setMore);
        } else {
            //获取不在考核范围之内的人员相关信息
            getNoCheckInfo(map, personnelScorrings, personnelUsers,year,month);
        }
    }

    private void uploadExcelSuccess(ModelMap map, String year, String month, List<PersonnelScorringInfo> personnelScorringsNew, List<PersonnelScorring> personnelScorringList,
                                    List<PersonnelUser> personnelUsers, List<PersonnelScorringInfo> scorrings,ExcelLog excelLog) {
        for (int i = 0; i < personnelScorringsNew.size(); i++) {
            for (int j = 0; j < personnelUsers.size(); j++) {
                //组织部导入的用户信息
                PersonnelScorringInfo info = personnelScorringsNew.get(i);
                String moneyCard = info.getMoneycard().replaceAll(" ", "");
                String userName = info.getUsername().replaceAll(" ", "");
                String departmentName = info.getDepartmentname().replaceAll(" ", "");
                String notes = info.getNotes().replaceAll(" ", "");
                info.setDepartmentname(departmentName);
                info.setMoneycard(moneyCard);
                info.setUsername(userName);
                info.setNotes(notes);
                //人事部导入的用户信息
                PersonnelUser user = personnelUsers.get(j);
                if (moneyCard.equals(user.getMoneycard()) /*&&
                        userName.equals(user.getUsername()) &&
                        departmentName.equals(user.getDepartmentname())*/) {
                    scorrings.add(info);
                }
            }
        }
        personnelScorringsNew.removeAll(scorrings);
        if (personnelScorringsNew.size() == 0) {
            List<Integer> usercodes = new ArrayList<>();
            if (personnelScorringList.size() > 0) {
                for (PersonnelScorring personnelScorring : personnelScorringList) {
                    usercodes.add(personnelScorring.getUsercode());
                }
            }
            addPersonScorring(year, month, scorrings, map, usercodes,excelLog);
        }
    }

    private void getErrorInfo(ModelMap map, List<PersonnelScorringInfo> personnelScorrings) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < personnelScorrings.size(); i++) {
            if (i == personnelScorrings.size() - 1) {
                str.append(personnelScorrings.get(i).getUsername() + "(" + personnelScorrings.get(i).getMoneycard() + "-" + personnelScorrings.get(i).getDepartmentname() + ")");
            } else {
                str.append(personnelScorrings.get(i).getUsername() + "(" + personnelScorrings.get(i).getMoneycard() + "-" + personnelScorrings.get(i).getDepartmentname() + ")").append("\n");

            }
        }
        map.put("msg", str + "信息有误，请与人事部联系进行核对");
        map.put("code", 1);
    }

    private void getLessUploadInfo(ModelMap map, List<PersonnelScorringInfo> personnelScorrings, List<PersonnelUser> personnelUsers) {
        List<PersonnelUser> users = new ArrayList<>();
        for (int i = 0; i < personnelUsers.size(); i++) {
            for (int j = 0; j < personnelScorrings.size(); j++) {//查找少上传的数据
                String moneycard = personnelScorrings.get(j).getMoneycard().replaceAll(" ", "");
                if (personnelUsers.get(i).getMoneycard().equals(moneycard)) {
                    users.add(personnelUsers.get(i));
                }
            }
        }
        personnelUsers.removeAll(users);
        StringBuilder str = new StringBuilder();
        if (personnelUsers.size() > 5) {
            map.put("msg", "多人还未上传考核分数，请与人事部进行联系");
            map.put("code", 1);
        } else {
            for (int i = 0; i < personnelUsers.size(); i++) {
                if (i == personnelUsers.size() - 1) {
                    str.append(personnelUsers.get(i).getUsername() + "(" + personnelUsers.get(i).getMoneycard() + "-" + personnelUsers.get(i).getDepartmentname() + ")");
                } else {
                    str.append(personnelUsers.get(i).getUsername() + "(" + personnelUsers.get(i).getMoneycard() + "-" + personnelUsers.get(i).getDepartmentname() + ")").append("\n");

                }
            }
            map.put("msg", str + "还未上传考核分数，请与人事部进行联系");
            map.put("code", 1);
        }
    }

    private void getNoCheckInfo(ModelMap map, List<PersonnelScorringInfo> personnelScorrings, List<PersonnelUser> personnelUsers,
                                String year,String month) {
        PersonnelScorring personnel=new PersonnelScorring();
        personnel.setYear(year);
        personnel.setMonth(month);
        List<PersonnelScorringInfo> scorrings = new ArrayList<>();
        for (int i = 0; i < personnelScorrings.size(); i++) {
            for (int j = 0; j < personnelUsers.size(); j++) {//查找多余的数据
                if (personnelScorrings.get(i).getMoneycard().equals(personnelUsers.get(j).getMoneycard())) {
                    scorrings.add(personnelScorrings.get(i));
                }
            }
        }
        personnelScorrings.removeAll(scorrings);
        StringBuilder str = new StringBuilder();//不在考核范围之内
        StringBuilder sb = new StringBuilder();//在其他部门打过分
        for (int i = 0; i < personnelScorrings.size(); i++) {
            personnel.setMoneycard(personnelScorrings.get(i).getMoneycard());
            PersonnelScorring scorring = personnelScorringService.selectOne(personnel);
            if (i == scorrings.size() - 1) {
                if (scorring!=null){
                    sb.append(scorring.getUsername() + "(" + scorring.getMoneycard() + ")已在" + scorring.getDepartmentname()+"被打过分,分数为"+scorring.getScore() + ";");
                }else {
                    str.append(personnelScorrings.get(i).getUsername() + "(" + personnelScorrings.get(i).getMoneycard() + "-" + personnelScorrings.get(i).getDepartmentname() + ")");
                }
            } else {
                if (scorring!=null){
                    sb.append(scorring.getUsername() + "(" + scorring.getMoneycard() + ")已在" + scorring.getDepartmentname()+"被打过分,分数为"+scorring.getScore() + ";");
                }else {
                    str.append(personnelScorrings.get(i).getUsername() + "(" + personnelScorrings.get(i).getMoneycard() + "-" + personnelScorrings.get(i).getDepartmentname() + ")").append("\n");
                }
            }
        }
        //查出成员在其它部门的信息
        if (sb.toString().equals("")) {
            map.put("msg", str + "不在考核范围之内，请与人事部进行联系");
        }else {
            map.put("msg", sb + "如有疑问请与人事部进行联系");
        }
        map.put("code", 1);
    }

    private void getRepeatData(ModelMap map, Set<String> setMore) {
        StringBuilder str = new StringBuilder();
        Iterator it = setMore.iterator();
        while (it.hasNext()) {
            String next = (String) it.next();
            if (next != null || !next.isEmpty()) {
                PersonnelUser user = personnelUserService.selectByPrimaryKey(next);
                str.append(user.getUsername() + "(" + user.getMoneycard() + "-" + user.getDepartmentname() + ")").append("\n");
            }
        }
        map.put("msg", str + "重复上传，请仔细核查后重新上传");
        map.put("code", 1);
    }


    private void addPersonScorring(String year, String month, List<PersonnelScorringInfo> personnelScorrings,
                                   ModelMap map, List<Integer> usercodes,ExcelLog excelLog) {
        List<PersonnelScorring> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        List<String> user = new ArrayList<>();
        for (PersonnelScorringInfo personnelScorring : personnelScorrings) {
            if (personnelScorring.getScore().equals("")) {
                user.add(personnelScorring.getUsername());
            }
        }
        if (user.size() == 0) {
            for (PersonnelScorringInfo personnelScorring : personnelScorrings) {
                PersonnelScorring scorring = new PersonnelScorring();
                scorring.setYear(year);
                scorring.setMonth(month);
                scorring.setMoneycard(String.valueOf(personnelScorring.getMoneycard()));
                scorring.setUsername(personnelScorring.getUsername());
                scorring.setDepartmentname(personnelScorring.getDepartmentname());
                scorring.setScore(personnelScorring.getScore());
                scorring.setNotes(personnelScorring.getNotes());
                list.add(scorring);
            }
            if (list.size() > 0) {
                if (usercodes.size() > 0) {//批量删除
                    personnelScorringService.batchDelete(usercodes);
                }
                personnelScorringService.batchInsert(list);
            }
            //新增分数excel行为日志记录
            excelLogService.insertSelective(excelLog);
            map.put("msg", "提交成功");
            map.put("code", 0);
        } else if (user.size() <= 5) {
            for (String str : user) {
                sb.append(str).append("未打分;");
            }
            sb.append("请检查并重新上传");
            map.put("msg", sb);
            map.put("code", 1);

        } else {
            map.put("msg", "检查到多个用户未打分，请检查并重新上传");
            map.put("code", 1);
        }
    }

    /**
     * 部门长导出分数excel表
     *
     * @param req
     * @param response
     * @param personnelScorring
     * @param info
     * @return
     */
    @RequestMapping(value = "/exportExcel", produces = "application/json;charset=utf-8")
    public Object exportExcel(HttpServletRequest req, HttpServletResponse response,
                              PersonnelScorring personnelScorring, String info) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                User user = userService.findRaterUserByUserCode(usercode);
                Rater rater = raterService.selectByMoneyCard(user.getMoneycard());
                List<PersonnelScorring> personnelScorrings;
                // 定义表的标题
                String title = "评分列表";
                //定义表的列名
                String[] rowsName = new String[]{"序号", "发薪号", "用户姓名", "所属科室", "分数", "年份", "季度"};
                //定义表的内容
                List<Object[]> dataList = new ArrayList<>();

                Object[] objs;
                //json字符串转对象
                JSONObject jsonObject = JSONObject.fromObject(info);
                PersonnelScorringInfoLike personneInfo = (PersonnelScorringInfoLike) JSONObject.toBean(jsonObject, PersonnelScorringInfoLike.class);
                personnelScorring.setYear(personneInfo.getYear());
                personnelScorring.setMonth(personneInfo.getMonth());
                personnelScorrings = personnelScorringService.selectAllLike(personneInfo.getYear(), personneInfo.getMonth(), rater.getDepartment(), personneInfo.getScore1(), personneInfo.getScore2());
                for (int i = 0; i < personnelScorrings.size(); i++) {
                    objs = new Object[rowsName.length];
                    objs[0] = i;
                    objs[1] = personnelScorrings.get(i).getMoneycard();
                    objs[2] = personnelScorrings.get(i).getUsername();
                    objs[3] = personnelScorrings.get(i).getDepartmentname();
                    objs[4] = personnelScorrings.get(i).getScore();
                    objs[5] = personnelScorrings.get(i).getYear();
                    objs[6] = personnelScorrings.get(i).getMonth();
                    dataList.add(objs);
                }
                // 创建ExportExcel对象
                ExcelUtil excelUtil = new ExcelUtil();
                String text = rater.getDepartment() + "评分列表文档.xls";
                String fileName = new String(text.getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
                excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
                map.put("msg", "导出成功");
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "导出失败");
                map.put("code", 1);
            }
        }
        return map;
    }

    /**
     * 查询所有人事评分数据(导出)
     *
     * @param req
     * @param year
     * @param month
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectExportAllLike")
    public Object selectExportAllLike(HttpServletRequest req, String year, String month,
                                      String score1, String score2, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        try {
            String usercode = (String) req.getSession().getAttribute("usercode");
            List<PersonnelScorring> personnelScorrings;
            if (usercode != null) {
                User user = userService.findRaterUserByUserCode(usercode);
                Rater rater = raterService.selectByMoneyCard(user.getMoneycard());
                List<UserRoleKey> role = userRoleService.selectUserRoleByUserCode(user.getUsercode());
                boolean flag = false;
                if (rater != null) {
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400") && !rater.getDepartment().equals("人事处")) {
                            flag = true;
                        }
                    }

                } else {
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400")) {
                            flag = true;
                        }
                    }
                }
                PageHelper.startPage(pageNum, pageSize);
                if (flag == true) {
                    personnelScorrings = personnelScorringService.selectAllLike(year, month, null, score1, score2);
                } else {
                    personnelScorrings = personnelScorringService.selectAllLike(year, month, rater.getDepartment(), score1, score2);
                }
                PageInfo<PersonnelScorring> pageInfo = new PageInfo<>(personnelScorrings);
                personnelScorrings = pageInfo.getList();
                map.put("pageTotals", pageInfo.getTotal());
                map.put("data", personnelScorrings);
                map.put("msg", "查询成功");
                map.put("code", 0);
            } else {
                map.put("msg", "登录用户过期，请重新登陆");
                map.put("code", 810);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 根据年份查找所有的评分数据
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectHistoryPersonnelScorring")
    public Object selectHistoryPersonnelScorring(HttpServletRequest req, int pageNum, int pageSize, PersonnelScorring personnelScorring) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                PageHelper.startPage(pageNum, pageSize);
                getPersonnelList(personnelScorring, map);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户过期，请重新登陆");
            map.put("code", 810);
        }
        return map;
    }

    private void getPersonnelList(PersonnelScorring personnelScorring, ModelMap map) {
        List<PersonnelScorring> personnelScorrings = personnelScorringService.selectListByMonthAndYear(personnelScorring);
        PageInfo<PersonnelScorring> pageInfo = new PageInfo<>(personnelScorrings);
        judgePersonnelScorringsIsNull(map, personnelScorrings, pageInfo);
    }

    private void judgePersonnelScorringsIsNull(ModelMap map, List<PersonnelScorring> personnelScorrings, PageInfo<PersonnelScorring> pageInfo) {
        if (personnelScorrings.size() == 0) {
            map.put("msg", "暂无数据");
            map.put("code", 0);
        } else {
            map.put("totalPages", pageInfo.getTotal());
            map.put("data", personnelScorrings);
            map.put("code", 0);
        }
    }


    /**
     * 管理员导出人员分数表
     *
     * @param response
     * @param scorring
     * @param info
     */
    @RequestMapping(value = "/exportScore")
    public void exportExcelData1(HttpServletResponse response, PersonnelScorring scorring, String info) {
        List<PersonnelScorring> list;
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        PersonnelInfo personnel = (PersonnelInfo) JSONObject.toBean(jsonObject, PersonnelInfo.class);
        scorring.setUsername(personnel.getUsername());
        scorring.setDepartmentname(personnel.getDepartmentname());
        scorring.setYear(personnel.getYear());
        scorring.setMonth(personnel.getMonth());
        scorring.setScore1(personnel.getScore1());
        scorring.setScore2(personnel.getScore2());

        list = personnelScorringService.selectListByMonthAndYear(scorring);
        // 创建ExportExcel对象
        try {
            // 獲取工作表
            Workbook workbook = exportBigDataExcel(list);
            // 完成下載
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<PersonnelScorring> employeeList) {
        // 1.获取数据
        // 2.创建工作簿
        // 阈值，内存中的对象数量最大值，超过这个值会生成一个临时文件存放到硬盘中
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("人事部-人员评分列表");
        String[] titles = {"序号", "发薪号", "用户姓名", "所属部门", "分数", "年份", "季度"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.从集合中取数据并赋值
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            PersonnelScorring employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getMoneycard());
            row.createCell(2).setCellValue(employee.getUsername());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getScore());
            row.createCell(5).setCellValue(employee.getYear());
            row.createCell(6).setCellValue(employee.getMonth());
        }
        return wb;

    }

    private void downFile(ByteArrayOutputStream os, HttpServletResponse response) throws IOException {
        String fileName = "人事部-人员评分列表.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }


}
