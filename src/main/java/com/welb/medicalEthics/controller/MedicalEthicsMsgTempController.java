package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.entity.MedicalEthicsMsgTemp;
import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.entity.PartyBranchRelations;
import com.welb.medicalEthics.entity.RecordEntity;
import com.welb.medicalEthics.mapper.MedicalEthicsMsgTempMapper;
import com.welb.medicalEthics.service.MedicalEthicsMsgService;
import com.welb.medicalEthics.service.MedicalEthicsMsgTempService;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import com.welb.medicalEthics.service.PartyBranchRelationsService;
import com.welb.medicalEthics.service.impl.MedicalEthicsUserImpl;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.personnel_check.entity.ExcelLog;
import com.welb.personnel_check.service.IExcelLogService;
import com.welb.sysBase.entity.BranchDepartment;
import com.welb.sysBase.entity.EvaluationDepartmentUser;
import com.welb.sysBase.service.BranchDepartmentService;
import com.welb.sysBase.service.EvaluationDepartmentUserService;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.Constant;
import com.welb.sysBase.util.PageUtils;
import com.welb.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static com.welb.sysBase.util.Constant.*;

@RestController
@RequestMapping("/medicalEthicsMsgTemp")
public class MedicalEthicsMsgTempController extends BaseController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private MedicalEthicsMsgTempService medicalEthicsMsgTempService;

    @Autowired
    private MedicalEthicsMsgService msgService;

    @Autowired
    private IExcelLogService excelLogService;

    @Autowired
    private MedicalEthicsUserService medicalEthicsUserService;

    @Autowired
    private IUserService userService;

    @Autowired
    private BranchDepartmentService branchDepartmentService;

    @Autowired
    private PartyBranchRelationsService partyBranchRelationsService;

    @Autowired
    private HUserService personneService;

    @Autowired
    private EvaluationDepartmentUserService departmentUserService;

    @Autowired(required = false)
    private MedicalEthicsMsgTempMapper medicalEthicsMsgTempMapper;

    @RequestMapping("/list")
    public Object list() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            List<MedicalEthicsMsgTemp> dataSource = medicalEthicsMsgTempService.list(new HashMap<>(param));
            List<MedicalEthicsMsgTemp> listData = PageUtils.PaginationSettingForPages(param, dataSource);
            map.put("totalPages", dataSource.size());
            map.put("data", listData);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * 手动添加考核人员-临时
     */
    @RequestMapping("/add")
    public Object add() {
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        String username = param.get("userName").trim();
        String userId = param.get("userId").trim();
        String departmentId = param.get("departmentId");
        String personType =  param.get("personType");

        if(StringUtils.isEmpty(username)){
            map.put("code",1);
            map.put("msg","用户名不能为空");
            return ajaxJson(map);
        }

        if(StringUtils.isEmpty(userId)){
            map.put("code",1);
            map.put("msg","用户名不能为空");
            return ajaxJson(map);
        }

        if(StringUtils.isEmpty(departmentId)){
            map.put("code",1);
            map.put("msg","部门名不能为空");
            return ajaxJson(map);
        }

        if(StringUtils.isEmpty(personType)){
            map.put("code",1);
            map.put("msg","人员类型不能为空");
            return ajaxJson(map);
        }

        MedicalEthicsMsgTemp medicalEthicsMsgTemp = medicalEthicsMsgTempService.selectByUserId(userId);
        if(medicalEthicsMsgTemp != null){
            map.put("code",1);
            map.put("msg","发薪号已经存在");
            return ajaxJson(map);
        }

        if(!personneService.checkIdAndName(Integer.parseInt(userId), username)){
            map.put("code",1);
            map.put("msg","用户名和发薪号不匹配");
            return ajaxJson(map);
        }else{
            MedicalEthicsMsgTemp newData = new MedicalEthicsMsgTemp();
            BranchDepartment branchDepartment = branchDepartmentService.selectByDepartmentId(Integer.parseInt(departmentId));
            if(branchDepartment != null){
                //支部
                Integer branchId = branchDepartment.getBranchId();
                PartyBranchRelations branchParty = partyBranchRelationsService.selectById(branchId);
                if(branchParty != null){
                    //总支部
                    PartyBranchRelations generalParty = partyBranchRelationsService.selectById(branchParty.getParentId());
                    if(generalParty != null){
                        //党委
                        PartyBranchRelations committeedParty = partyBranchRelationsService.selectById(generalParty.getParentId());
                        if(committeedParty != null){
                            newData.setPartyCommitteesId(committeedParty.getId());
                            newData.setGeneralBranchId(generalParty.getId());
                            newData.setBranchId(branchParty.getId());
                            newData.setStatus(STATUS_ZERO);
                            newData.setUserId(userId);
                            newData.setUserName(username);
                            newData.setPersonType(Integer.parseInt(personType));
                            try {
                                //新增临时用户
                                medicalEthicsMsgTempService.insert(newData);
                                EvaluationDepartmentUser departmentUser = new EvaluationDepartmentUser();
                                departmentUser.setDepartmentId(Integer.parseInt(departmentId));
                                departmentUser.setUserId(Integer.parseInt(userId));
                                departmentUserService.bindNewUser(departmentUser);
                                map.put("msg", "操作成功");
                                map.put("code", 0);
                                return ajaxJson(map);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            map.put("code",1);
            map.put("msg","操作失败");
        }
        return ajaxJson(map);
    }

    @RequestMapping("/delete")
    public Object delete() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            medicalEthicsMsgTempService.delete(Integer.valueOf(param.get("id")));
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * update 临时用户更新
     *
     * @return {@link Object}
     */
    @RequestMapping("/update")
    public Object update() {
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        //人员类型
        String personType =  param.get("personType");
        //发薪号
        String userId = param.get("userId").trim();
        //科室id
        String departmentId = param.get("departmentId");

        if(StringUtils.isEmpty(departmentId)){
            map.put("code",1);
            map.put("msg","部门名不能为空");
            return ajaxJson(map);
        }

        if(StringUtils.isEmpty(personType)){
            map.put("code",1);
            map.put("msg","人员类型不能为空");
            return ajaxJson(map);
        }
        try {
            msgService.updateMsg(userId,departmentId,personType);
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            map.put("code",1);
            map.put("msg","操作失败");
        }
        return ajaxJson(map);
    }

    /**
     * updateMsg 正式员工更新
     *
     * @return {@link Object}
     */
    @RequestMapping("/updateMsg")
    public Object updateMsg() {
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        String personType =  param.get("personType");
        String userId = param.get("userId").trim();
        String departmentId = param.get("departmentId");
        if(StringUtils.isEmpty(departmentId)){
            map.put("code",1);
            map.put("msg","部门名不能为空");
            return ajaxJson(map);
        }
        if(StringUtils.isEmpty(personType)){
            map.put("code",1);
            map.put("msg","人员类型不能为空");
            return ajaxJson(map);
        }
        try {
            msgService.updateMsg(userId,departmentId,personType);
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            e.printStackTrace();
            map.put("code",1);
            map.put("msg","操作失败");
        }
        return ajaxJson(map);
    }

    @RequestMapping("/downloadExcel")
    public void downloadExcel(HttpServletResponse response) {
        ModelMap map = new ModelMap();
        try {
            ExcelUtils.exportExcel(response,
                    MEDICAL_ETHICS_MSG_SHEET_NAME,
                    MEDICAL_ETHICS_MSG_TEMPLATE);
            map.put("msg", "下载模板成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.getTrace(e));
            map.put("msg", "下载模板失败");
            map.put("code", 1);
        }
    }

    @RequestMapping(value = "/readExcel", method = RequestMethod.POST)
    @ResponseBody
    public Object readExcel(@RequestParam("file") MultipartFile file) throws Exception {
        ModelMap map = new ModelMap();
        String filePath = "C:\\fileupload";
        String fileName = FileUpload.fileUp(file, filePath, "temp");
        PageData pageData = this.getPageData();
        String loginUserId = pageData.getString("u_id");
        try {
            List<PageData> dataSource = (List) ObjectExcelRead.readExcelForSpecDateFormat(filePath, fileName, 1, 0, 0, DATE_FORMAT_PATTERN);
            //主表数据
            List<MedicalEthicsMsgTemp> insertData = new ArrayList<>();
            //字段验证结果
            List<String> resultList = new ArrayList<>();
            //验证数据
            this.handleExcelData(dataSource, insertData, resultList, loginUserId);
            //插入到DB
            if (resultList.isEmpty()) {
                List<String> rsMsg = medicalEthicsMsgTempService.batchInsert(insertData);
                if (rsMsg.isEmpty()) {
                    log.info("遍历持久化导入数据：" + dataSource.size() + "条");
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    LocalDate now = LocalDate.now();
                    ExcelLog excelLog = new ExcelLog();
                    excelLog.setSavepath(filePath);
                    excelLog.setOperatetime(format.format(new Date()));
                    excelLog.setYear(String.valueOf(now.getYear()));
                    excelLog.setMonth(String.valueOf(now.getMonth()));
                    excelLog.setMoneycard(loginUserId);
                    excelLogService.insertSelective(excelLog);
                    map.put("code", 0);
                    map.put("msg", "导入成功");
                } else {
                    map.put("code", 1);
                    map.put("msg", rsMsg);
                }
            } else {
                map.put("msg", resultList);
                map.put("code", 5);
            }
        } catch (Exception e) {
            map.put("msg", "异常");
            map.put("code", 4);
            e.printStackTrace();
        }
        ObjectExcelRead.deleteDirectory(filePath);
        return ajaxJson(map);
    }

    private void handleExcelData(List<PageData> dataSource, List<MedicalEthicsMsgTemp> insertData, List<String> resultList, String loginUserId) {
        if (dataSource.isEmpty()) {
            //数据为空
            resultList.add("导入数据为空,请修改后上传!");
        } else {
            User user = userService.getUserByUserCode(loginUserId);
            List<MedicalEthicsUser> userList = medicalEthicsUserService.list(new HashMap<String, String>() {{
                put("userId", user.getMoneycard());
            }});
            //登录用户,如果支部信息为空
            MedicalEthicsUser medicalEthicsUser = null;
            if (userList.isEmpty()) {
                resultList.add("当前用户没有医德医风权限");
            } else {
                //获取到拥有支部书记的角色
                for (MedicalEthicsUser u : userList) {
                    if ("101".equals(u.getRoleCode()) && u.getBranchId() != 0) {
                        medicalEthicsUser = u;
                        break;
                    }
                }
            }

            if (medicalEthicsUser == null) {
                resultList.add("未获取到当前用户党支部信息,请使用支部书记登录");
            } else {
                List<RecordEntity> recordList = new ArrayList<>();
                for (int i = 0; i < dataSource.size(); i++) {
                    MedicalEthicsMsgTemp medicalEthicsMsgTemp = new MedicalEthicsMsgTemp();
                    //起始行
                    int row = i + 2;
                    PageData pageData = dataSource.get(i);
                    //用户姓名
                    String userName = ExportExcelUtils.removeSpace(pageData.getString("var0"));
                    medicalEthicsMsgTemp.setUserName(userName);
                    //发薪号
                    String userId = ExportExcelUtils.removeSpace(pageData.getString("var1"));
                    ExportExcelUtils.checkItemIsEmpty(userId, row, 2, "发薪号", resultList);
                    ExportExcelUtils.checkItemIsNumAndLength(userId, row, 2, "发薪号", false, 0, resultList);
                    //验证发薪号是否存在
                    if (resultList.isEmpty()) {
                        //验证发薪号和用户名是否匹配
                        if (personneService.checkIdAndName(Integer.parseInt(userId), userName)) {
                            medicalEthicsMsgTemp.setUserId(userId);
                        } else {
                            resultList.add("姓名:" + userName + "[" + userId + "]不正确,请核对发薪号后添加");
                        }
                        //党委id
                        medicalEthicsMsgTemp.setPartyCommitteesId(medicalEthicsUser.getPartyCommitteesId());
                        //党总支id
                        medicalEthicsMsgTemp.setGeneralBranchId(medicalEthicsUser.getGeneralBranchId());
                        //党支部id
                        medicalEthicsMsgTemp.setBranchId(medicalEthicsUser.getBranchId());
                        //根据的登录用户获取人员类型
                        medicalEthicsMsgTemp.setPersonType(MedicalEthicsUserImpl.loadPersonType(medicalEthicsUser));
                        //添加到数据源
                        insertData.add(medicalEthicsMsgTemp);
                        //验证重复数据
                        this.checkRepeatData(row, userId, userName, recordList, resultList);
                        //添加到历史记录
                        recordList.add(new RecordEntity(userId, userName, row));
                    }
                }
            }
        }
    }

    private void checkRepeatData(int curRow, String userId, String userName, List<RecordEntity> recordList, List<String> resultList) {
        for (RecordEntity record : recordList) {
            if (userId.equals(record.getUserId()) && userName.equals(record.getName())) {
                resultList.add("在上传的Excel中,第" + record.getRow() + "行和第" + curRow + "行的[" + userName + "-" + userId + "]数据重复,请修改后上传");
                break;
            }
        }
    }
}
