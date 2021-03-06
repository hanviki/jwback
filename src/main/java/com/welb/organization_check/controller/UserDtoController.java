package com.welb.organization_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.info.Info;
import com.welb.organization_check.service.*;
import com.welb.util.CalendarUtil;
import com.welb.util.DateUtil;
import com.welb.util.ExcelUtil;
import com.welb.util.LogUtil;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: UserDtoController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/416:48
 */
@RestController
@RequestMapping("/history")
public class UserDtoController {
    private final Logger log = LogManager.getLogger(this.getClass());
    private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    IUserDtoService dtoService;
    @Resource
    IMonthSummaryService summaryService;
    @Resource
    IStationService stationService;
    @Resource
    IDepartmentService departmentService;
    @Resource
    IScoreFlowService flowService;
    @Resource
    IScoreHistoryService historyService;
    @Resource
    IManualSetTimeService setTimeService;
    @Resource
    IUserService userService;

    /**
     * ?????????????????????????????? ?????????????????????????????????
     *
     * @param dto
     * @param pageNum
     * @param pageSize
     * @return
     */

  /*  @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLike(HttpServletRequest req, UserDto dto, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<UserDto> userDtoList;
            try {
                getCurrentgetGradeState(state);
                PageHelper.startPage(pageNum, pageSize);
                userDtoList = dtoService.selectUserDtoLike(dto);
                getStationName(userDtoList);
                PageInfo<UserDto> pageInfo = new PageInfo<>(userDtoList);
                userDtoList = pageInfo.getList();
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "????????????????????????");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "????????????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;

    }
*/
    /**
     * ??????????????????????????????  ??????????????????????????????
     *
     * @param dto
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/scorelist", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLikeByUserAndState(HttpServletRequest req, UserDto dto, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            try {
                //????????????
                String year = CalendarUtil.getYear();
                //????????????
                String month = CalendarUtil.getMonth();
                //??????????????????
                String quarter =month;// CalendarUtil.getQuarter(month);
                int counts = Integer.parseInt(quarter.trim()) - 1;
                //????????????????????????
                String sysTime = DateUtil.getTime();

                    //????????????-????????????????????????
                    manualSelectUserDtoLikeByUserAndState(dto, map, year, quarter, counts, sysTime, pageNum, pageSize);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "??????????????????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;
    }

    private void manualSelectUserDtoLikeByUserAndState(UserDto dto, ModelMap map, String year, String quarter,
                                                       int counts, String sysTime, int pageNum, int pageSize) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
        if (setTime != null) {

                //????????????????????????
                month = quarter;
                getUserDtos(dto, map, setTime.getYear(), setTime.getMonth(), pageNum, pageSize);



        }
    }

    private void getUserDtos(UserDto dto, ModelMap map, String year, String month, int pageNum, int pageSize) {
        List<UserDto> userDtos;
        dto.setYear(year);
        dto.setMonth(month);
        PageHelper.startPage(pageNum, pageSize);
        userDtos = dtoService.selectUserDtoLike(dto);
        getStationName(userDtos);
        PageInfo<UserDto> pageInfo = new PageInfo<>(userDtos);
        userDtos = pageInfo.getList();
        map.put("totalPages", pageInfo.getTotal());
        map.put("msg", "??????????????????????????????");
        map.put("data", userDtos);
        map.put("code", 0);
    }

    private void automaticSelectUserDtoLikeByUserAndState(UserDto dto, ModelMap map, String year, int counts, int pageNum, int pageSize) {
        String month;
        if (counts == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";

        } else {
            month = String.valueOf(counts);
        }
        getUserDtos(dto, map, year, month, pageNum, pageSize);
    }

    /**
     * ???????????????????????? ????????? ?????? ??????
     *
     * @param userDtoList
     */
    private void getStationName(List<UserDto> userDtoList) {
        //??????????????????
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
                    //????????????  ??????= A????????? X A?????????????????? +  B????????? X B?????????????????? + C????????? X C?????????????????? + D????????? X D??????????????????

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
     * ??????????????????????????????
     *
     * @param serialnos
     * @return
     */
    @RequestMapping(value = "/updateFinishGradeBySerialNo", produces = "application/json;charset=utf-8")
    public Object updateFinishGradeBySerialNo(String serialnos) {
        ModelMap map = new ModelMap();
        String[] serialno = serialnos.split(",");

        int counts = 0;
        for (int i = 0; i < serialno.length; i++) {

            int count = summaryService.updateFinishGradeBySerialNo(serialno[i]);
            counts += count;
        }
        if (counts > 0) {
            map.put("msg", "??????????????????????????????????????????");
            map.put("code", 0);
        } else {
            map.put("msg", "??????????????????????????????????????????");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * ????????????????????????????????????????????????
     *
     * @return
     */
    @RequestMapping(value = "/updateFinishGradeAll", produces = "application/json;charset=utf-8")
    public Object updateFinishGradeAll() {
        ModelMap map = new ModelMap();
        int count = summaryService.updateFinishGradeAll();
        if (count > 0) {
            map.put("msg", "????????????????????????????????????");
            map.put("code", 0);
        } else {
            map.put("msg", "????????????????????????????????????");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * ??????????????????excel???
     *
     * @param response
     */
    @RequestMapping(value = "exportScore", produces = "application/json;charset=utf-8")
    public void exportExcelData(HttpServletRequest req, HttpServletResponse response, UserDto dto, String info) throws ParseException {
        String state = (String) req.getSession().getAttribute("state");
        // ??????????????????
        String title = "????????????-????????????";
        //??????????????????
        String[] rowsName = new String[]{"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????"};
        //??????????????????
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs = null;
        //json??????????????????
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        //????????????
        String year = CalendarUtil.getYear();
        //????????????
        String month = CalendarUtil.getMonth();
        //??????????????????
        String quarter = month;//CalendarUtil.getQuarter(month);
        int counts = Integer.parseInt(quarter.trim()) - 1;
        //????????????????????????
        String sysTime = DateUtil.getTime();

            //????????????-????????????????????????
            manualExportExcelData(dto, rowsName, dataList, year, quarter, counts, sysTime);


        try {
            // ??????ExportExcel??????
            ExcelUtil excelUtil = new ExcelUtil();
            String fileName = new String("??????????????????.xls".getBytes("UTF-8"), "iso-8859-1");    //??????word??????????????????
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }
    }

    private void manualExportExcelData(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, String quarter, int counts, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
        if (setTime != null) {

                //????????????????????????
                month = quarter;
                getUserDtos(dto, rowsName, dataList, setTime.getYear(), setTime.getMonth());

        }
    }

    private void getUserDtos(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, String month) {
        List<UserDto> userDtos;
        dto.setYear(year);
        dto.setMonth(month);
        userDtos = dtoService.selectUserDtoLike(dto);
        // ???????????????????????? ????????? ?????? ??????
        getStationName(userDtos);
        //????????????????????????
        getUserDtosInfo(userDtos, rowsName, dataList);
    }

    private void automaticExportExcelData(UserDto dto, String[] rowsName, List<Object[]> dataList, String year, int counts) {
        String month;
        List<UserDto> userDtos;
        if (counts == 0) {
            int lastyear = Integer.parseInt(year.trim()) - 1;
            year = String.valueOf(lastyear);
            month = "12";
        } else {
            month = String.valueOf(counts);
        }
        getUserDtos(dto, rowsName, dataList, year, month);
    }

    /**
     * ????????????????????????
     *
     * @param userDtos
     * @param rowsName
     * @param dataList
     */
    private void getUserDtosInfo(List<UserDto> userDtos, String[] rowsName, List<Object[]> dataList) {
        Object[] objs;
        for (int i = 0; i < userDtos.size(); i++) {
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = userDtos.get(i).getUsername();
            objs[2] = userDtos.get(i).getMoneycard();
            objs[3] = userDtos.get(i).getDepartmentname();
            objs[4] = userDtos.get(i).getStationname();
            objs[5] = userDtos.get(i).getStatename();
            objs[6] = userDtos.get(i).getAScore();
            objs[7] = userDtos.get(i).getBScore();
            objs[8] = userDtos.get(i).getCScore();
            objs[9] = userDtos.get(i).getDScore();
            objs[10] = userDtos.get(i).getTotalScore();
            dataList.add(objs);
        }
    }

   /* @RequestMapping(value = "/exportHistoryScore", produces = "application/json;charset=utf-8")
    public void exportExcelData1(HttpServletResponse response, UserDto dto, String info) {
        List<UserDto> userDtos;
        //json??????????????????
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = dtoService.selectUserDtoLike(dto);
        getStationName(userDtos);
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportBigDataExcel(userDtos);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<UserDto> employeeList) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("??????????????????");
        String[] titles = {"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            UserDto employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getScorestatusname());
            row.createCell(6).setCellValue(employee.getStatename());
            row.createCell(7).setCellValue(employee.getYear() + "-" + employee.getMonth());
            if (employee.getAScore() == null) {
                employee.setAScore(0.0);
            }
            if (employee.getBScore() == null) {
                employee.setBScore(0.0);
            }
            if (employee.getCScore() == null) {
                employee.setCScore(0.0);
            }
            if (employee.getDScore() == null) {
                employee.setDScore(0.0);
            }
            if (employee.getTotalScore() == null) {
                employee.setTotalScore(0.0);
            }

            row.createCell(8).setCellValue(employee.getAScore());
            row.createCell(9).setCellValue(employee.getBScore());
            row.createCell(10).setCellValue(employee.getCScore());
            row.createCell(11).setCellValue(employee.getDScore());
            row.createCell(12).setCellValue(employee.getTotalScore());
        }
        return wb;

    }

    private void downFile(ByteArrayOutputStream os, HttpServletResponse response) throws IOException {
        String fileName = "??????????????????.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }*/



   /* private void getCurrentgetGradeState(String state) {
        try {
            UserDto userDto = new UserDto();
            String year = CalendarUtil.getYear();
            String month = CalendarUtil.getMonth();
            String quarter = CalendarUtil.getQuarter(month);
            int i = Integer.parseInt(quarter) - 1;
            //????????????????????????
            String sysTime = DateUtil.getTime();
            if (state.equals("1")) {
                //????????????
                manualGetGrade(userDto, year, quarter, i, sysTime);

            } else {
                //????????????
                automaticgetGrage(userDto, year, i);
            }
            List<UserDto> userDtos = dtoService.gradeList(userDto);
            getGradeState(userDtos);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
        }

    }

    private void manualGetGrade(UserDto userDto, String year, String quarter, int i, String sysTime) throws ParseException {
        String month;
        ManualSetTime setTime = setTimeService.selectManualByYearAndMonth(year, quarter);
        if (setTime != null) {
            if (sdfTime.parse(sysTime).getTime() >= sdfTime.parse(setTime.getTime()).getTime()) {
                //????????????????????????
                month = quarter;
                userDto.setMonth(month);
                userDto.setYear(year);
            } else {
                //?????????????????????????????????????????????????????????
                automaticgetGrage(userDto, year, i);
            }
        }else {
            if (i == 0) {
                int lastyear = Integer.parseInt(year.trim()) - 1;
                year = String.valueOf(lastyear);
                month = "4";
            } else {
                month = String.valueOf(i);
            }
            userDto.setYear(year);
            userDto.setMonth(month);
        }
    }*/

    private void automaticgetGrage(UserDto userDto, String year, int i) {
        String month;
        if (i == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "12";
        } else {
            month = String.valueOf(i);
        }
        userDto.setYear(year);
        userDto.setMonth(month);
    }


   /* @RequestMapping(value = "/gradeList", produces = "application/json;charset=utf-8")
    public Object gradeList(HttpServletRequest req, UserDto dto, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<UserDto> userDtoList;
            try {
                getCurrentgetGradeState(state);
                PageHelper.startPage(pageNum, pageSize);
                userDtoList = dtoService.gradeList(dto);
//                getGradeState(userDtoList);
                PageInfo<UserDto> pageInfo = new PageInfo<>(userDtoList);
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "????????????");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;

    }

    private void getGradeState(List<UserDto> userDtoList) {
        List<MonthSummary> list = new ArrayList<>();
        for (UserDto userDto : userDtoList) {
            MonthSummary summary = new MonthSummary();
            summary.setSerialno(userDto.getSerialno());
            userDto.setEmployeecode(userDto.getUsercode());
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
*/
/*
    @RequestMapping(value = "/exportGradeExcel", produces = "application/json;charset=utf-8")
    public void exportGradeExcel(HttpServletResponse response, UserDto dto, String info) {
        List<UserDto> userDtos;
        //json??????????????????
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = dtoService.gradeList(dto);
        getStationName(userDtos);
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportGradeBigDataExcel(userDtos);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            gradeDownFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private SXSSFWorkbook exportGradeBigDataExcel(List<UserDto> employeeList) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("??????????????????");
        String[] titles = {"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "????????????"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            UserDto employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getScorestatusname());
            row.createCell(6).setCellValue(employee.getYear() + "-" + employee.getMonth());
        }
        return wb;

    }

    private void gradeDownFile(ByteArrayOutputStream os, HttpServletResponse response) throws IOException {
        String fileName = "??????????????????????????????.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }*/


    @RequestMapping(value = "/list", produces = "application/json;charset=utf-8")
    public Object selectUserDtoLike(HttpServletRequest req, ScoreHistory history, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<ScoreHistory> historieList;
            try {
                //???????????????
                initUserList(map, state);
                PageHelper.startPage(pageNum, pageSize);
                historieList = historyService.selectHistoryList(history);
                PageInfo<ScoreHistory> pageInfo = new PageInfo<>(historieList);
                historieList = pageInfo.getList();
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "????????????????????????");
                map.put("data", historieList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "????????????????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;

    }

    private void initUserList(ModelMap map, String state) throws ParseException {
        List<User> users = userService.findUserAllBySummary();
        //??????????????????
        String year = CalendarUtil.getYear();
        //??????????????????
        String month = CalendarUtil.getMonth();
        //??????????????????
        String quarter = month;//CalendarUtil.getQuarter(month);
        //?????????????????????
        int count = Integer.parseInt(quarter.trim()) - 1;
        //????????????????????????
        String sysTime = DateUtil.getTime();

        ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "");

        ScoreHistory scoreHistory=new ScoreHistory();
        getCurrentYearAndMonth(state, manualSetTime.getYear(), manualSetTime.getMonth(), count, sysTime, scoreHistory);
        List<ScoreHistory> historyList = historyService.selectUserHisotyList(scoreHistory);
        if (users.size()==0){
            map.put("msg", "????????????");
            map.put("code", 0);
        }else if (users.size()>historyList.size()){
            List<ScoreHistory>scoreHistoryList=new ArrayList<>();
            for (User user:users){
                scoreHistory.setUsercode(user.getUsercode());
                ScoreHistory one = historyService.selectOneByHistory(scoreHistory);
                if (one==null){
                    ScoreHistory score=new ScoreHistory();
                    score.setUsercode(user.getUsercode());
                    score.setYear(scoreHistory.getYear());
                    score.setMonth(scoreHistory.getMonth());
                    score.setScorestatus("1");
                    score.setAscore(0.0);
                    score.setBscore(0.0);
                    score.setCscore(0.0);
                    score.setDscore(0.0);
                    score.setTotalscore(0.0);
                    scoreHistoryList.add(score);
                }
            }
            if (scoreHistoryList.size()>0){
                historyService.batchInsert(scoreHistoryList);
            }

        }
    }

    /**
     * ????????????????????????????????????
     * @param req
     * @param history
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/gradeList", produces = "application/json;charset=utf-8")
    public Object gradeList(HttpServletRequest req, ScoreHistory history , int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        String state = (String) req.getSession().getAttribute("state");
        if (usercode != null) {
            List<ScoreHistory> userDtoList;
            try {
                //???????????????
                initGradeList(map, state);
                PageHelper.startPage(pageNum, pageSize);
                userDtoList = historyService.gradeList(history);
                PageInfo<ScoreHistory> pageInfo = new PageInfo<>(userDtoList);
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "????????????");
                map.put("data", userDtoList);
                map.put("code", 0);

            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("error", e.getMessage());
                map.put("msg", "????????????");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "??????????????????,???????????????");
            map.put("code", 810);
        }
        return map;

    }

    private void initGradeList(ModelMap map, String state) throws ParseException {
        List<User> users = userService.selectGradeUserList();
        //??????????????????
        String year = CalendarUtil.getYear();
        //??????????????????
        String month = CalendarUtil.getMonth();
        //??????????????????
        String quarter =month;// CalendarUtil.getQuarter(month);
        //?????????????????????
        int count = Integer.parseInt(quarter.trim()) - 1;
        //????????????????????????
        String sysTime = DateUtil.getTime();
        ManualSetTime manualSetTime = setTimeService.selectManualByYearAndMonth("", "");
        ScoreHistory scoreHistory=new ScoreHistory();
        getCurrentYearAndMonth(state, manualSetTime.getYear(), manualSetTime.getMonth(), count, sysTime, scoreHistory);

        List<ScoreHistory> historyList = historyService.selectGradeHisotyList(scoreHistory);
        if (users.size()==0){
            map.put("msg", "???????????????");
            map.put("code", 0);
        }else if (users.size()>historyList.size()){
            List<ScoreHistory>scoreHistoryList=new ArrayList<>();
            for (User user:users){
                scoreHistory.setUsercode(user.getUsercode());
                ScoreHistory one = historyService.selectOneByHistory(scoreHistory);
                if (one==null){
                    ScoreHistory score=new ScoreHistory();
                    score.setUsercode(user.getUsercode());
                    score.setYear(scoreHistory.getYear());
                    score.setMonth(scoreHistory.getMonth());
                    score.setScorestatus("1");
                    scoreHistoryList.add(score);
                }
            }
            if (scoreHistoryList.size()>0){
                historyService.batchInsert(scoreHistoryList);
            }

        }
    }

    private void getCurrentYearAndMonth(String state, String year, String quarter, int count, String sysTime, ScoreHistory scoreHistory) throws ParseException {
        String month;

            //????????????-????????????????????????
            ManualSetTime setTime = setTimeService.selectManualByYearAndMonth("", "");
            if (setTime != null) {

                //????????????????????????
                month = quarter;
                scoreHistory.setYear(setTime.getYear());
                scoreHistory.setMonth(setTime.getMonth());

            }

    }


    @RequestMapping(value = "/exportGradeExcel", produces = "application/json;charset=utf-8")
    public void exportGradeExcel(HttpServletResponse response, ScoreHistory dto, String info) {
        List<ScoreHistory> userDtos;
        //json??????????????????
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = historyService.gradeList(dto);
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportGradeBigDataExcel(userDtos);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            gradeDownFile(os, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private SXSSFWorkbook exportGradeBigDataExcel(List<ScoreHistory> employeeList) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("??????????????????");
        String[] titles = {"??????", "????????????", "?????????", "????????????", "????????????", "????????????", "????????????"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            ScoreHistory employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getScorestatusname());
            row.createCell(6).setCellValue(employee.getYear() + "-" + employee.getMonth());
        }
        return wb;

    }

    private void gradeDownFile(ByteArrayOutputStream os, HttpServletResponse response) throws IOException {
        String fileName = "??????????????????????????????.xls";
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }




    @RequestMapping(value = "/exportHistoryScore", produces = "application/json;charset=utf-8")
    public void exportExcelData1(HttpServletResponse response, ScoreHistory dto, String info) {
        List<ScoreHistory> userDtos;
        //json??????????????????
        JSONObject jsonObject = JSONObject.fromObject(info);
        Info info1 = (Info) JSONObject.toBean(jsonObject, Info.class);
        dto.setUsername(info1.getUsername());
        dto.setStationcode(info1.getStationcode());
        dto.setYear(info1.getYear());
        dto.setMonth(info1.getMonth());
        dto.setState(info1.getState());
        dto.setScorestatus(info1.getScorestatus());
        userDtos = historyService.selectHistoryList(dto);
        // ??????ExportExcel??????
        try {
            // ???????????????
            Workbook workbook = exportBigDataExcel(userDtos);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);

            downFile(os, response,"??????????????????.xls");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private SXSSFWorkbook exportBigDataExcel(List<ScoreHistory> employeeList) {
        // 1.????????????
        // 2.???????????????
        // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
        SXSSFWorkbook wb = new SXSSFWorkbook(100);
        Sheet sheet = wb.createSheet("sheet1");
        String[] titles = {"??????", "????????????", "?????????", "????????????", "????????????","??????", "????????????", "????????????", "????????????", "A", "B", "C", "D", "??????"};
        Row titleRow = sheet.createRow(0);
        for (int i = 0; i < titles.length; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(titles[i]);
        }
        // 3.??????????????????????????????
        for (int i = 0; i < employeeList.size(); i++) {
            Row row = sheet.createRow(i + 1);
            ScoreHistory employee = employeeList.get(i);
            row.createCell(0).setCellValue(i + 1);
            row.createCell(1).setCellValue(employee.getUsername());
            row.createCell(2).setCellValue(employee.getMoneycard());
            row.createCell(3).setCellValue(employee.getDepartmentname());
            row.createCell(4).setCellValue(employee.getStationname());
            row.createCell(5).setCellValue(employee.getRolename());
            row.createCell(6).setCellValue(employee.getScorestatusname());
            row.createCell(7).setCellValue(employee.getStatename());
            row.createCell(8).setCellValue(employee.getYear() + "-" + employee.getMonth());
            if (employee.getAscore() == null) {
                employee.setAscore(0.0);
            }
            if (employee.getBscore() == null) {
                employee.setBscore(0.0);
            }
            if (employee.getCscore() == null) {
                employee.setCscore(0.0);
            }
            if (employee.getDscore() == null) {
                employee.setDscore(0.0);
            }
            if (employee.getTotalscore() == null) {
                employee.setTotalscore(0.0);
            }
            row.createCell(9).setCellValue(employee.getAscore());
            row.createCell(10).setCellValue(employee.getBscore());
            row.createCell(11).setCellValue(employee.getCscore());
            row.createCell(12).setCellValue(employee.getDscore());
            row.createCell(13).setCellValue(employee.getTotalscore());
        }
        return wb;

    }

    private void downFile(ByteArrayOutputStream os, HttpServletResponse response,String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1"));
        ServletOutputStream outputStream = response.getOutputStream();
        os.writeTo(outputStream);
        os.close();
        outputStream.flush();

    }


    /**
     * ????????????????????????????????????????????????????????????
     * @return
     */
    @RequestMapping("/oneClickDown")
    public Object oneClickDown(HttpServletResponse response,String year,String month){
        ModelMap map=new ModelMap();
        try {
            List<ScoreHistory> list = historyService.oneClickDown(year, month);
            Workbook workbook = exportBigDataExcel(list);
            // ????????????
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            downFile(os, response,"??????????????????????????????.xls");
        }catch (Exception e){
            log.error(LogUtil.getTrace(e));
            map.put("msg", "??????????????????");
            map.put("error", e.getMessage());
            map.put("eor", e.getStackTrace());
            map.put("code", 1);
        }
        return map;
    }
}
