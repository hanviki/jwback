package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.service.*;
import com.welb.medicalEthics.vo.CalculateScoreLevelVo;
import com.welb.medicalEthics.vo.CheckResultVo;
import com.welb.medicalEthics.dto.CurrentExcellentDto;
import com.welb.medicalEthics.vo.EducationLevelVo;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.DepartmentUserVo;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.PageUtils;
import com.welb.util.DateUtil;
import com.welb.util.StringUtil;
import com.welb.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.welb.sysBase.util.Constant.*;

/**
 * @description: 统计查询相关
 * @author: luox
 * @date： 2020/12/22
 */

@RestController
@Slf4j
@RequestMapping("/calculate")
public class CalculateController extends BaseController {

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired
    private IUserService userService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private MedicalEthicsDicService dicService;


    /**
     * getCurrentExcellentInfo 查询当前支部的优秀人数/总人数等信息
     *
     * @return {@link Object}
     */
    @RequestMapping("/currentExcellentInfo")
    public Object getCurrentExcellentInfo() {
        Map<String, String> param = Tools.getParamMap(request);
        String branchId = param.get("branchId");
        ModelMap map = new ModelMap();
        if (StringUtils.isEmpty(branchId)) {
            map.put("msg", "未获取到支部信息");
            map.put("code", 1);
        } else {
            try {
                CurrentExcellentDto currentExcellentInfo = partyBranchRelationsService.getCurrentExcellentInfo(branchId);
                map.put("code", 0);
                map.put("msg", "查询成功");
                map.put("data", currentExcellentInfo);
            } catch (Exception e) {
                map.put("code", 1);
                map.put("msg", e.getMessage());
                e.printStackTrace();
            }
        }
        return ajaxJson(map);
    }

    /**
     * 详情跳转
     *
     * @return
     */
    @RequestMapping("/checkResultDetail")
    public Object checkResultDetail() {
        ModelMap map = new ModelMap();
        Map<String, String> params = Tools.getParamMap(request);
        String level = params.get("level");
        String educationLevel = params.get("educationLevel");
        //分页参数
        Map<String, String> pageParams = new HashMap<String, String>() {{
            put("pageSize", params.get("pageSize"));
            put("pageNum", params.get("pageNum"));
        }};
        {
            Map<String, Object> queryParam = new HashMap<>();
            String username = params.get("username");
            String uid = params.get("userId");
            String personType = params.get("personType");
            String branchId = params.get("branchId");
            String departmentId = params.get("departmentId");
            String minAge = params.get("minAge");
            String maxAge = params.get("maxAge");
            String title = params.get("title");
            String politicalStatus = params.get("politicalStatus");

            if (StringUtils.isNotEmpty(username)) {
                queryParam.put("username", username);
            }

            if (StringUtils.isNotEmpty(uid)) {
                queryParam.put("userId", uid);
            }

            if (StringUtils.isNotEmpty(personType)) {
                queryParam.put("personType", personType);
            }

            if (StringUtils.isNotEmpty(branchId)) {
                queryParam.put("branchId", branchId);
            }

            //多科室查询
            if (StringUtils.isNotEmpty(departmentId)) {
                List<String> departmentIdList = Arrays.asList(departmentId.split(COMMA));
                queryParam.put("departmentIdList", departmentIdList);
            }
            //返回所有的数据
            List<CheckResultVo> checkResultVoList = departmentUserService.checkResultByType(queryParam);
            List resultList = new ArrayList<>();
            List<CheckResultVo> returnList = new ArrayList<>();
            if (!checkResultVoList.isEmpty()) {
                extendDetailInfo(checkResultVoList);
            }

            if (StringUtils.isNotEmpty(level)) {
                if ("0".equals(level)) {
                    checkResultVoList = checkResultVoList.stream().filter(c -> c.getIsFinish() == 0).collect(Collectors.toList());
                } else {
                    checkResultVoList = checkResultVoList.stream().filter(c -> c.getLevel() == Integer.parseInt(level) && c.getIsFinish() == 1).collect(Collectors.toList());
                }
            }

            if (StringUtils.isNotEmpty(educationLevel)) {
                String[] split = educationLevel.split(",");
                List<String> educationLevelList = Arrays.asList(split);
                checkResultVoList = checkResultVoList.stream().filter(c -> educationLevelList.contains(c.getEducationLevel())).collect(Collectors.toList());
            }

            if (StringUtil.isNotEmpty(maxAge) && (!"0".equals(maxAge))) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getAge() <= Integer.parseInt(maxAge)).collect(Collectors.toList());
            }

            if (StringUtil.isNotEmpty(minAge) && (!"0".equals(minAge))) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getAge() >= Integer.parseInt(minAge)).collect(Collectors.toList());
            }

            if (StringUtils.isNotEmpty(title)) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getTitle() == Integer.parseInt(title)).collect(Collectors.toList());
            }

            if (StringUtils.isNotEmpty(politicalStatus)) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getPoliticalStatus().equals(politicalStatus)).collect(Collectors.toList());
            }

            resultList = checkResultVoList;
            returnList = PageUtils.PaginationSettingForPages(pageParams, resultList);
            returnList.forEach(r -> {
                if (r.getIsFinish() == 1) {
                    r.setLevelName(dicService.selectByCodeAndType(DIC_TYPE_SCORE_LEVEL, String.valueOf(r.getLevel())));
                } else {
                    r.setLevelName("未完成");
                }
                r.setEducationLevelName(dicService.selectByCodeAndType(DIC_TYPE_EDUCATION_LEVEL, r.getEducationLevel()));
                r.setPoliticalStatusName(dicService.selectByCodeAndType(DIC_TYPE_POLITICAL_STATUS, r.getPoliticalStatus()));
                r.setTitleName(dicService.selectByCodeAndType(DIC_TYPE_TITLE, String.valueOf(r.getTitle())));
            });
            map.put("code", 0);
            map.put("data", returnList);
            map.put("totalPages", resultList.size());
            map.put("msg", "查询成功");
            return ajaxJson(map);
        }
    }

    /**
     * 检查是否已完成
     * @param vo
     * @return
     */
    public boolean checkIsFinish(CheckResultVo vo) {
        if ("0".equals(vo.getPersonType())) {
            if (vo.getStep() != null) {
                return vo.getStep() == 5;
            }
        } else if ("1".equals(vo.getPersonType())) {
            if (vo.getStep() != null) {
                return vo.getStep() == 3;
            }
        }
        return false;

    }


    @RequestMapping("/checkResult")
    public Object checkResult() {
        ModelMap map = new ModelMap();
        Map<String, String> params = Tools.getParamMap(request);
        String userCode = request.getHeader("u_id");
        User user = userService.getUserByUserCode(userCode);
        String level = params.get("level");
        String educationLevel = params.get("educationLevel");
        //分页参数
        Map<String, String> pageParams = new HashMap<String, String>() {{
            put("pageSize", params.get("pageSize"));
            put("pageNum", params.get("pageNum"));
        }};
        if (null == user) {
            map.put("code", 1);
            map.put("msg", "查询失败,用户不存在");
            return ajaxJson(map);
        } else {
            Map<String, Object> queryParam = new HashMap<>();
            String username = params.get("username");
            String uid = params.get("userId");
            String personType = params.get("personType");
            String branchId = params.get("branchId");
            String departmentId = params.get("departmentId");
            String minAge = params.get("minAge");
            String maxAge = params.get("maxAge");
            String title = params.get("title");
            String politicalStatus = params.get("politicalStatus");

            if (StringUtils.isNotEmpty(username)) {
                queryParam.put("username", username);
            }

            if (StringUtils.isNotEmpty(uid)) {
                queryParam.put("userId", uid);
            }

            if (StringUtils.isNotEmpty(personType)) {
                queryParam.put("personType", personType);
            }

            if (StringUtils.isNotEmpty(branchId)) {
                queryParam.put("branchId", branchId);
            }

            //多科室查询
            if (StringUtils.isNotEmpty(departmentId)) {
                List<String> departmentIdList = Arrays.asList(departmentId.split(COMMA));
                queryParam.put("departmentIdList", departmentIdList);
            }
            //返回所有的数据
            List<CheckResultVo> checkResultVoList = departmentUserService.selectCheckDetailByParams(queryParam);
            List resultList = new ArrayList<>();
            List<CheckResultVo> returnList = new ArrayList<>();
            if (!checkResultVoList.isEmpty()) {
                if ((StringUtil.isNotEmpty(maxAge) && (!"0".equals(maxAge))) || (StringUtil.isNotEmpty(minAge) && (!"0".equals(minAge)))) {
                    extendDetailInfo(checkResultVoList);
                }
            }
            if (StringUtils.isNotEmpty(level)) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getLevel() == Integer.parseInt(level)).collect(Collectors.toList());
            }

            if (StringUtils.isNotEmpty(educationLevel)) {
                String[] split = educationLevel.split(",");
                List<String> educationLevelList = Arrays.asList(split);
                checkResultVoList = checkResultVoList.stream().filter(c -> educationLevelList.contains(c.getEducationLevel())).collect(Collectors.toList());
            }

            if (StringUtil.isNotEmpty(maxAge) && (!"0".equals(maxAge))) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getAge() <= Integer.parseInt(maxAge)).collect(Collectors.toList());
            }

            if (StringUtil.isNotEmpty(minAge) && (!"0".equals(minAge))) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getAge() >= Integer.parseInt(minAge)).collect(Collectors.toList());
            }

            if (StringUtils.isNotEmpty(title)) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getTitle() == Integer.parseInt(title)).collect(Collectors.toList());
            }

            if (StringUtils.isNotEmpty(politicalStatus)) {
                checkResultVoList = checkResultVoList.stream().filter(c -> c.getPoliticalStatus().equals(politicalStatus)).collect(Collectors.toList());
            }

            resultList = checkResultVoList;
            returnList = PageUtils.PaginationSettingForPages(pageParams, resultList);
            returnList.forEach(r -> {
                r.setLevelName(dicService.selectByCodeAndType(DIC_TYPE_SCORE_LEVEL, String.valueOf(r.getLevel())));
                r.setEducationLevelName(dicService.selectByCodeAndType(DIC_TYPE_EDUCATION_LEVEL, r.getEducationLevel()));
                r.setPoliticalStatusName(dicService.selectByCodeAndType(DIC_TYPE_POLITICAL_STATUS, r.getPoliticalStatus()));
                r.setTitleName(dicService.selectByCodeAndType(DIC_TYPE_TITLE, String.valueOf(r.getTitle())));
            });
            map.put("code", 0);
            map.put("data", returnList);
            map.put("totalPages", resultList.size());
            map.put("msg", "查询成功");
            return ajaxJson(map);
        }
    }


    @RequestMapping("/exportCheckResult")
    public void exportCheckResult(HttpServletResponse resp) throws IOException {
        Map<String, String> params = Tools.getParamMap(request);
        String level = params.get("level");
        String educationLevel = params.get("educationLevel");
        Map<String, Object> queryParam = new HashMap<>();
        String username = params.get("username");
        String uid = params.get("userId");
        String personType = params.get("personType");
        String branchId = params.get("branchId");
        String departmentId = params.get("departmentId");
        String minAge = params.get("minAge");
        String maxAge = params.get("maxAge");
        String title = params.get("title");
        String politicalStatus = params.get("politicalStatus");

        if (StringUtils.isNotEmpty(username)) {
            queryParam.put("username", username);
        }

        if (StringUtils.isNotEmpty(uid)) {
            queryParam.put("userId", uid);
        }

        if (StringUtils.isNotEmpty(personType)) {
            queryParam.put("personType", personType);
        }

        if (StringUtils.isNotEmpty(branchId)) {
            queryParam.put("branchId", branchId);
        }

        //多科室查询
        if (StringUtils.isNotEmpty(departmentId)) {
            List<String> departmentIdList = Arrays.asList(departmentId.split(COMMA));
            queryParam.put("departmentIdList", departmentIdList);
        }
        //返回所有的数据
        List<CheckResultVo> checkResultVoList = departmentUserService.selectCheckDetailByParams(queryParam);
        List resultList = new ArrayList<>();
        List<CheckResultVo> returnList = new ArrayList<>();
        if (!checkResultVoList.isEmpty()) {
            extendDicInfo(checkResultVoList);
        }

        if (StringUtils.isNotEmpty(level)) {
            checkResultVoList = checkResultVoList.stream().filter(c -> c.getLevel() == Integer.parseInt(level)).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(educationLevel)) {
            String[] split = educationLevel.split(",");
            List<String> educationLevelList = Arrays.asList(split);
            checkResultVoList = checkResultVoList.stream().filter(c -> educationLevelList.contains(c.getEducationLevel())).collect(Collectors.toList());
        }

        if (StringUtil.isNotEmpty(maxAge) && (!"0".equals(maxAge))) {
            checkResultVoList = checkResultVoList.stream().filter(c -> c.getAge() <= Integer.parseInt(maxAge)).collect(Collectors.toList());
        }

        if (StringUtil.isNotEmpty(minAge) && (!"0".equals(minAge))) {
            checkResultVoList = checkResultVoList.stream().filter(c -> c.getAge() >= Integer.parseInt(minAge)).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(title)) {
            checkResultVoList = checkResultVoList.stream().filter(c -> c.getTitle() == Integer.parseInt(title)).collect(Collectors.toList());
        }

        if (StringUtils.isNotEmpty(politicalStatus)) {
            checkResultVoList = checkResultVoList.stream().filter(c -> c.getPoliticalStatus().equals(politicalStatus)).collect(Collectors.toList());
        }

        resultList = checkResultVoList;
        if (!resultList.isEmpty()) {
            String fileName = "医德考评结果汇总.xls";
            resp.setHeader("Content-disposition", "attachment;filename="
                    + new String(fileName.getBytes("gb2312"), "ISO8859-1"));    //设置文件头编码格式
            resp.setContentType("APPLICATION/OCTET-STREAM;charset=UTF-8");//设置类型
            resp.setHeader("Cache-Control", "no-cache");//设置头
            resp.setDateHeader("Expires", 0);//设置日期头

            HSSFWorkbook book = new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet();
            sheet.setColumnWidth(0, 8 * 256);
            sheet.setColumnWidth(1, 12 * 256);
            sheet.setColumnWidth(2, 12 * 256);
            sheet.setColumnWidth(3, 12 * 256);
            sheet.setColumnWidth(4, 12 * 256);
            sheet.setColumnWidth(5, 12 * 256);
            sheet.setColumnWidth(6, 12 * 256);
            sheet.setColumnWidth(7, 12 * 256);
            sheet.setColumnWidth(8, 20 * 256);
            sheet.setColumnWidth(9, 16 * 256);
            sheet.setColumnWidth(10, 12 * 256);
            sheet.setColumnWidth(11, 12 * 256);
            CellStyle cellStyle = book.createCellStyle();
            cellStyle.setDataFormat(book.createDataFormat().getFormat("yyyy-MM-dd"));
//            cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
            //首行字体
            HSSFFont headFot = book.createFont();
            headFot.setFontName("黑体");
//            headFot.setFontHeightInPoints((short) 12);//设置字体大小
            //首行样式
            CellStyle headStyle = book.createCellStyle();
            headStyle.setFont(headFot);

            String[] header = {"姓名", "发薪号", "出生年月", "政治面貌", "文化程度", "职称", "聘用时间", "考核年份", "科室", "所在党支部", "人员类型", "考核结果"};

            Row row0 = sheet.createRow(0);
            for (int i = 0; i < header.length; i++) {
                Cell cell01 = row0.createCell(i);
                cell01.setCellStyle(headStyle);
                cell01.setCellValue(header[i]);
            }
            int index = 1;
            for (int i = 0; i < checkResultVoList.size(); i++) {
                CheckResultVo resultData = checkResultVoList.get(i);
                Row row = sheet.createRow(index);
                //用户名
                Cell cell1 = row.createCell(0);
                cell1.setCellValue(resultData.getUsername());
                cell1.setCellStyle(cellStyle);
                //发薪号
                Cell cell2 = row.createCell(1);
                cell2.setCellValue(resultData.getUserId());
//                cell2.setCellStyle(cellStyle);
                //出生年月
                Cell cell3 = row.createCell(2);
                cell3.setCellValue(resultData.getBirth());
                cell3.setCellStyle(cellStyle);
                //政治面貌
                Cell cell4 = row.createCell(3);
                cell4.setCellValue(resultData.getPoliticalStatusName());
                cell4.setCellStyle(cellStyle);
                //文化程度
                Cell cell5 = row.createCell(4);
                cell5.setCellValue(resultData.getEducationLevelName());
                cell5.setCellStyle(cellStyle);
                //职称
                Cell cell6 = row.createCell(5);
                cell6.setCellValue(resultData.getTitleName());
                cell6.setCellStyle(cellStyle);
                //聘用时间
                Cell cell7 = row.createCell(6);
                cell7.setCellValue(resultData.getHireDate());
                cell7.setCellStyle(cellStyle);
                //考核年份
                Cell cell8 = row.createCell(7);
                cell8.setCellValue(resultData.getYear());
                cell8.setCellStyle(cellStyle);
                //科室名字
                Cell cell9 = row.createCell(8);
                cell9.setCellValue(resultData.getDepartmentName());
                cell9.setCellStyle(cellStyle);
                //所在党支部
                Cell cell10 = row.createCell(9);
                cell10.setCellValue(resultData.getPartyBranchName());
                cell10.setCellStyle(cellStyle);
                //员工类型
                Cell cell11 = row.createCell(10);
                String personTypeName = "0".equals(resultData.getPersonType()) ? "临床人员" : "非临床人员";
                cell11.setCellValue(personTypeName);
                cell1.setCellStyle(cellStyle);
                //考核结果
                Cell cell12 = row.createCell(11);
                cell12.setCellValue(resultData.getLevelName());
                cell12.setCellStyle(cellStyle);
                index++;
            }
            book.write(resp.getOutputStream());
            resp.getOutputStream().flush();
            resp.getOutputStream().close();
        }

    }

    /**
     * extendStatus 获取科室人员列表的step
     *
     * @param originList originList
     * @return {@link List<DepartmentUserVo>}
     */
    private List<CheckResultVo> extendDetailInfo(List<CheckResultVo> originList) {
        for (CheckResultVo resultVo : originList) {
            if (resultVo.getBirth() != null) {
                resultVo.setAge(DateUtil.getAge(resultVo.getBirth()));
            }
            if (checkIsFinish(resultVo)) {
                resultVo.setIsFinish(1);
            } else {
                resultVo.setIsFinish(0);
            }
        }
        return originList;
    }

    private List<CheckResultVo> extendDicInfo(List<CheckResultVo> originList) {
        for (CheckResultVo v : originList) {
            v.setTitleName(dicService.selectByCodeAndType(DIC_TYPE_TITLE, String.valueOf(v.getTitle())));
            v.setPoliticalStatusName(dicService.selectByCodeAndType(DIC_TYPE_POLITICAL_STATUS, v.getPoliticalStatus()));
            v.setEducationLevelName(dicService.selectByCodeAndType(DIC_TYPE_EDUCATION_LEVEL, v.getEducationLevel()));
            v.setLevelName(dicService.selectByCodeAndType(DIC_TYPE_SCORE_LEVEL, String.valueOf(v.getLevel())));
        }
        return originList;
    }

    /**
     * calculateEducationLevel教育程度统计
     *
     * @return {@link Object}
     */
    @RequestMapping("/educationLevel")
    public Object calculateEducationLevel() {
        List<EducationLevelVo> resList = new ArrayList<>();
        ModelMap map = new ModelMap();
        Map<String, String> params = Tools.getParamMap(request);
        String branchId = params.get("branchId");
        if (StringUtil.isNotEmpty(branchId)) {
            params.put("branchId", branchId);
        }
        try {
            //大专
            EducationLevelVo data1 = new EducationLevelVo();
            params.put("educationLevel", "1");
            int cliLevelOneCount = calculateService.cliCountByParams(params);
            int nonCliLevelOneCount = calculateService.nonCliCountByParams(params);
            int resLevelOne = cliLevelOneCount + nonCliLevelOneCount;
            data1.setValue(resLevelOne);
            resList.add(data1);
            //本科
            EducationLevelVo data2 = new EducationLevelVo();
            params.put("educationLevel", "2");
            int cliLevelTwoCount = calculateService.cliCountByParams(params);
            int nonCliLevelTwoCount = calculateService.nonCliCountByParams(params);
            int resLevelTwo = cliLevelTwoCount + nonCliLevelTwoCount;
            data2.setValue(resLevelTwo);
            resList.add(data2);
            //研究生
            EducationLevelVo data3 = new EducationLevelVo();
            params.put("educationLevel", "3");
            int cliLevelThreeCount = calculateService.cliCountByParams(params);
            int nonCliLevelThreeCount = calculateService.nonCliCountByParams(params);
            int resLevelThree = cliLevelThreeCount + nonCliLevelThreeCount;
            data3.setValue(resLevelThree);
            resList.add(data3);
            //博士
            EducationLevelVo data4 = new EducationLevelVo();
            params.put("educationLevel", "4");
            int cliLevelFourCount = calculateService.cliCountByParams(params);
            int nonCliLevelFourCount = calculateService.nonCliCountByParams(params);
            int resLevelFour = cliLevelFourCount + nonCliLevelFourCount;
            data4.setValue(resLevelFour);
            resList.add(data4);

            map.put("data", resList);
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 1);
        }
        return map;
    }

    /**
     * calculatePoliticalStatus 统计政治面貌
     *
     * @return {@link Object}
     */
    @RequestMapping("/politicalStatus")
    public Object calculatePoliticalStatus() {
        Map<String, String> params = new HashMap<>();
        List<EducationLevelVo> resList = new ArrayList<>();
        ModelMap map = new ModelMap();
        Map<String, String> reqParams = Tools.getParamMap(request);
        String branchId = reqParams.get("branchId");
        if (StringUtil.isNotEmpty(branchId)) {
            params.put("branchId", branchId);
        }
        try {
            //大专
            EducationLevelVo data1 = new EducationLevelVo();
            params.put("politicalStatus", "1");
            int levelOneCount = calculateService.cliCountByParams(params);
            int levelOneCountNon = calculateService.nonCliCountByParams(params);
            data1.setValue(levelOneCount + levelOneCountNon);
            resList.add(data1);
            //本科
            EducationLevelVo data2 = new EducationLevelVo();
            params.put("politicalStatus", "2");
            int levelTwoCount = calculateService.cliCountByParams(params);
            int levelTwoCountNon = calculateService.nonCliCountByParams(params);
            data2.setValue(levelTwoCount + levelTwoCountNon);
            resList.add(data2);

            EducationLevelVo data3 = new EducationLevelVo();
            params.put("politicalStatus", "3");
            int levelThreeCount = calculateService.cliCountByParams(params);
            int levelThreeCountNon = calculateService.nonCliCountByParams(params);
            data3.setValue(levelThreeCount + levelThreeCountNon);
            resList.add(data3);

            EducationLevelVo data4 = new EducationLevelVo();
            params.put("politicalStatus", "4");
            int levelFourCount = calculateService.cliCountByParams(params);
            int levelFourCountNon = calculateService.nonCliCountByParams(params);
            data4.setValue(levelFourCount + levelFourCountNon);
            resList.add(data4);

            EducationLevelVo data5 = new EducationLevelVo();
            params.put("politicalStatus", "5");
            int levelFiveCount = calculateService.cliCountByParams(params);
            int levelFiveCountNon = calculateService.nonCliCountByParams(params);
            data5.setValue(levelFiveCount + levelFiveCountNon);
            resList.add(data5);

            EducationLevelVo data6 = new EducationLevelVo();
            params.put("politicalStatus", "6");
            int levelSxiCount = calculateService.cliCountByParams(params);
            int levelSixCountNon = calculateService.nonCliCountByParams(params);
            data6.setValue(levelSxiCount + levelSixCountNon);
            resList.add(data6);

            EducationLevelVo data8 = new EducationLevelVo();
            params.put("politicalStatus", "8");
            int levelEightCount = calculateService.cliCountByParams(params);
            int levelEightCountNon = calculateService.nonCliCountByParams(params);
            data8.setValue(levelEightCount + levelEightCountNon);
            resList.add(data8);

            EducationLevelVo data9 = new EducationLevelVo();
            params.put("politicalStatus", "9");
            int levelNineCount = calculateService.cliCountByParams(params);
            int levelNineCountNon = calculateService.nonCliCountByParams(params);
            data9.setValue(levelNineCount + levelNineCountNon);
            resList.add(data9);

            EducationLevelVo data10 = new EducationLevelVo();
            params.put("politicalStatus", "10");
            int levelTenCount = calculateService.cliCountByParams(params);
            int levelTenCountNon = calculateService.nonCliCountByParams(params);
            data10.setValue(levelTenCount + levelTenCountNon);
            resList.add(data10);

            EducationLevelVo data11 = new EducationLevelVo();
            params.put("politicalStatus", "11");
            int levelElevenCount = calculateService.cliCountByParams(params);
            int levelElevenCountNon = calculateService.nonCliCountByParams(params);
            data11.setValue(levelElevenCount + levelElevenCountNon);
            resList.add(data11);

            EducationLevelVo data12 = new EducationLevelVo();
            params.put("politicalStatus", "12");
            int levelTwelveCount = calculateService.cliCountByParams(params);
            int levelTwelveCountNon = calculateService.nonCliCountByParams(params);
            data12.setValue(levelTwelveCount + levelTwelveCountNon);
            resList.add(data12);
            map.put("data", resList);
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 评分等级以及百分比
     *
     * @return
     */
    @RequestMapping("/scoreLevel")
    public Object calculateScoreLevel() {
        Map<String, Object> resData = new HashMap<>();

        List<CalculateScoreLevelVo> resList = new ArrayList<>();
        Map<String, String> cliQueryParams = new HashMap<>();
        Map<String, String> nonCliQueryParams = new HashMap<>();
        Map<String, String> params = Tools.getParamMap(request);
        String branchId = params.get("branchId");
        if (StringUtil.isNotEmpty(branchId)) {
            cliQueryParams.put("branchId", branchId);
            nonCliQueryParams.put("branchId", branchId);
        }
        cliQueryParams.put("year", CURRENT_YEAR);
        cliQueryParams.put("step", "5");
        nonCliQueryParams.put("year", CURRENT_YEAR);
        nonCliQueryParams.put("step", "3");

        try {
            cliQueryParams.put("level", "1");
            nonCliQueryParams.put("level", "1");
            int levelOneCli = calculateService.cliCountByParams(cliQueryParams);
            int levelOneNonCli = calculateService.nonCliCountByParams(nonCliQueryParams);

            cliQueryParams.put("level", "2");
            nonCliQueryParams.put("level", "2");
            int levelTwoCli = calculateService.cliCountByParams(cliQueryParams);
            int levelTwoNonCli = calculateService.nonCliCountByParams(nonCliQueryParams);

            cliQueryParams.put("level", "3");
            nonCliQueryParams.put("level", "3");
            int levelThreeCli = calculateService.cliCountByParams(cliQueryParams);
            int levelThreeNonCli = calculateService.nonCliCountByParams(nonCliQueryParams);

            cliQueryParams.put("level", "4");
            nonCliQueryParams.put("level", "4");
            int levelFourCli = calculateService.cliCountByParams(cliQueryParams);
            int levelFourNonCli = calculateService.nonCliCountByParams(nonCliQueryParams);

            int levelOneTotal = levelOneCli + levelOneNonCli;
            int levelTwoTotal = levelTwoCli + levelTwoNonCli;
            int levelThreeTotal = levelThreeCli + levelThreeNonCli;
            int levelFourTotal = levelFourCli + levelFourNonCli;

            int total = levelOneTotal + levelTwoTotal + levelThreeTotal + levelFourTotal;
            double levelOneTotalPercent = 0.0;
            double levelTwoTotalPercent = 0.0;
            double levelThreeTotalPercent = 0.0;
            double levelFourTotalPercent = 0.0;
            if (total > 0) {
                DecimalFormat df = new DecimalFormat("#.##");
                levelOneTotalPercent = Double.parseDouble(df.format((float) levelOneTotal / total * 100));
                levelTwoTotalPercent = Double.parseDouble(df.format((float) levelTwoTotal / total * 100));
                levelThreeTotalPercent = Double.parseDouble(df.format((float) levelThreeTotal / total * 100));
                levelFourTotalPercent = Double.parseDouble(df.format((float) (100 - levelOneTotalPercent - levelTwoTotalPercent - levelThreeTotalPercent)));
            }

            CalculateScoreLevelVo data1 = new CalculateScoreLevelVo();
            data1.setName(SCORE_LEVEL_ONE_TEXT + " " + levelOneTotalPercent + "%");
            data1.setValue(levelOneTotal + " 人");
            resList.add(data1);

            CalculateScoreLevelVo data2 = new CalculateScoreLevelVo();
            data2.setName(SCORE_LEVEL_TWO_TEXT + " " + levelTwoTotalPercent + "%");
            data2.setValue(levelTwoTotal + " 人");
            resList.add(data2);

            CalculateScoreLevelVo data3 = new CalculateScoreLevelVo();
            data3.setName(SCORE_LEVEL_THREE_TEXT + " " + levelThreeTotalPercent + "%");
            data3.setValue(levelThreeTotal + " 人");
            resList.add(data3);

            CalculateScoreLevelVo data4 = new CalculateScoreLevelVo();
            data4.setName(SCORE_LEVEL_FOUR_TEXT + " " + levelFourTotalPercent + "%");
            data4.setValue(levelFourTotal + " 人");
            resList.add(data4);
            resData.put("data", resList);
            resData.put("code", 0);
            resData.put("msg", "查询成功");
        } catch (NumberFormatException e) {
            resData.put("code", 1);
            resData.put("msg", "查询失败");
            e.printStackTrace();
        }
        return ajaxJson(resData);
    }


}
