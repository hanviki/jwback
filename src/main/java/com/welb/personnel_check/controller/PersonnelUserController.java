package com.welb.personnel_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IUserRoleService;
import com.welb.organization_check.service.IUserService;
import com.welb.personnel_check.entity.*;
import com.welb.personnel_check.info.PersonnelUserImport;
import com.welb.personnel_check.info.PersonnelUserInfo;
import com.welb.personnel_check.service.*;
import com.welb.util.*;
import net.sf.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: PersonnelUserController
 * @projectName xh-360appraisal-interface
 * @description: 人事部用户控制器
 * @date 2019/9/211:14
 */
@RestController
@RequestMapping("/personnelUser")
public class PersonnelUserController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IPersonnelUserService personnelUserService;
    @Resource
    IUserService userService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IRaterService raterService;
    @Resource
    IPersonnelScorringService personnelScorringService;
    @Resource
    IAuthorizationUserService authorizationUserService;
    @Resource
    IPersonnelAuthorizationService personnelAuthorizationService;

    /**
     * 查询人事部所有的用户
     *
     * @param personnelUser
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Object selectAllPersonnelUser(HttpServletRequest req, PersonnelUser personnelUser, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                PageHelper.startPage(pageNum, pageSize);
                List<PersonnelUser> personnelUsers = personnelUserService.selectAllPersonnelByLike(personnelUser);
                PageInfo<PersonnelUser> pageInfo = new PageInfo<>(personnelUsers);
                personnelUsers = pageInfo.getList();
                map.put("totalPages", pageInfo.getTotal());
                map.put("msg", "查询成功");
                map.put("data", personnelUsers);
                map.put("code", 0);

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

    /**
     * 导入用人事部户数据
     *
     * @return
     */
    @RequestMapping("/importExcel")
    public Object importExcel(@RequestParam("file") MultipartFile file, HttpServletRequest req) {
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
                map.put("msg", "上传成功");
                map.put("savepath", savepath);
                map.put("code", 0);
            } else {
                map.put("msg", "上传的文件格式不对,请上传.xls格式的excel表");
                map.put("code", 1);
            }

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "导入失败,请核对上传分模板是否正确");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 导入用人事部户数据
     *
     * @return
     */
    @RequestMapping("/confirmImportExcel")
    public Object confirmImportExcel(String savepath) {
        ModelMap map = new ModelMap();
        int startRow = 3;
        int endRow = 0;
        try {
            List<PersonnelUserImport> personnelUsers = (List<PersonnelUserImport>) ImportExcel.importExcel(savepath, startRow, endRow, PersonnelUserImport.class);
            List<PersonnelUser> list = new ArrayList<>();
            List<PersonnelUserImport> errorList = new ArrayList<>();//发薪号填写错误集合
            for (PersonnelUserImport personnelUser : personnelUsers) {
                try {
                    Integer.parseInt(personnelUser.getMoneycard());
                } catch (Exception e) {
                    errorList.add(personnelUser);
                    continue;
                }
            }
            if (errorList.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (PersonnelUserImport userImport : errorList) {
                    sb.append(userImport.getMoneycard() + "(" + userImport.getUsername() + "-" + userImport.getDepartmentname() + ");");
                }
                map.put("msg", sb + "发薪号填写错误，请仔细检查更正后重新上传");
                map.put("code", 1);
            } else {
                int flag = 0;
                flag = addPersonnelUser(personnelUsers, list, flag);
                if (flag == 1) {
                    map.put("msg", "上传的部分用户信息不完整，请仔细检查后重新上传");
                    map.put("code", 1);
                } else {
                    if (list.size() > 0) {
                        personnelUserService.batchInsert(list);
                        map.put("msg", "提交成功");
                        map.put("code", 0);
                    } else {
                        map.put("msg", "你已重复上传或上传的人员中在组织部已存在");
                        map.put("code", 1);
                    }
                }
            }

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "导入失败,请核对上传分模板是否正确");
            map.put("code", 1);
        }
        return map;
    }

    private int addPersonnelUser(List<PersonnelUserImport> personnelUsers, List<PersonnelUser> list, int flag) {
        for (PersonnelUserImport personnelUser : personnelUsers) {//匹配组织部的用户数据 ,如果有,就不添加
            if (personnelUser.getMoneycard().equals("") || personnelUser.getUsername().equals("")
                    || personnelUser.getDepartmentname().equals("")) {
                flag = 1;
                break;
            } else {
                User selectByMoneyCard = userService.selectByMoneyCard(personnelUser.getMoneycard());
                PersonnelUser personneluser = personnelUserService.selectByPrimaryKey(personnelUser.getMoneycard());
                if (selectByMoneyCard == null) {
                    judgeIsAddUser(list, personnelUser, personneluser);
                } else {
                    //判断是否是打分用户
                    List<UserRoleKey> roleList = userRoleService.selectUserRoleByUserCode(selectByMoneyCard.getUsercode());
                    if (roleList.size() > 0) {
                        for (UserRoleKey role : roleList) {
                            if (role.getRolecode().equals("150") || role.getRolecode().equals("500") || role.getRolecode().equals("400") || role.getRolecode().equals("700")) {
                                judgeIsAddUser(list, personnelUser, personneluser);
                            }
                        }
                    }
                }
            }

        }
        return flag;
    }


    private void judgeIsAddUser(List<PersonnelUser> list, PersonnelUserImport personnelUser, PersonnelUser personneluser) {
        if (personneluser == null) {
            PersonnelUser user = new PersonnelUser();
            user.setMoneycard(personnelUser.getMoneycard());
            user.setUsername(personnelUser.getUsername());
            user.setDepartmentname(personnelUser.getDepartmentname());
            user.setFlag("3");
            user.setEndtime("");
            user.setStarttime("");
            list.add(user);
        }
    }


    /**
     * 下载模板
     *
     * @param response
     * @return
     */
    @RequestMapping(value = "/downloadTemplate", produces = "application/json;charset=utf-8")
    public Object downloadTemplate(HttpServletResponse response) {
        ModelMap map = new ModelMap();
        // 定义表的标题
        String title = "行政后勤工作人员表";
        //定义表的列名
        String[] rowsName = new String[]{"序号", "发薪号", "用户姓名", "所属科室"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<>();
        // 创建ExportExcel对象
        ExcelUtil excelUtil = new ExcelUtil();

        try {
            String fileName = new String("行政后勤工作人员导入模板 .xls".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
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
     * 添加用户
     *
     * @param personnelUser
     * @return
     */
    @RequestMapping("/add")
    public Object addPersonnelUser(PersonnelUser personnelUser) {
        ModelMap map = new ModelMap();
        User user = userService.selectByMoneyCard(personnelUser.getMoneycard());
        PersonnelUser personnel = personnelUserService.selectByPrimaryKey(personnelUser.getMoneycard());
        if (user == null) {
            addPersonnel(personnelUser, map, personnel);
        } else {
            boolean flag = false;
            //判断是否是打分用户
            List<UserRoleKey> roleList = userRoleService.selectUserRoleByUserCode(user.getUsercode());
            if (roleList.size() > 0) {
                for (UserRoleKey role : roleList) {
                    if (role.getRolecode().equals("150") || role.getRolecode().equals("500") || role.getRolecode().equals("400") || role.getRolecode().equals("700")) {
                        flag = true;
                        break;
                    }
                }
            }
            if (flag == true) {
                addPersonnel(personnelUser, map, personnel);
            } else {
                map.put("msg", "您添加的用户是组织部成员但不是组织部打分用户,故不能添加该用户");
                map.put("code", 1);
            }
        }
        return map;
    }

    private void addPersonnel(PersonnelUser personnelUser, ModelMap map, PersonnelUser personnel) {
        if (personnel == null) {
            try {
                personnelUserService.insertSelective(personnelUser);
                addAthorizationUser(personnelUser);
                map.put("msg", "添加用户成功");
                map.put("code", 0);
            } catch (Exception e) {
                map.put("error", e.getMessage());
                map.put("msg", "添加用户失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "该用户已存在,请勿重复添加");
            map.put("code", 1);
        }
    }

    private void addAthorizationUser(PersonnelUser personnelUser) {
        AuthorizationUser user = new AuthorizationUser();
        user.setMoneycard(personnelUser.getMoneycard());
        user.setUsername(personnelUser.getUsername());
        user.setDepartmentname(personnelUser.getDepartmentname());
        user.setStarttime(personnelUser.getStarttime());
        user.setEndtime(personnelUser.getEndtime());
        user.setFlag("3");
        user.setIspersonnel("2");
        authorizationUserService.insertSelective(user);
    }


    /**
     * 修改用户数据
     *
     * @param personnelUser
     * @return
     */
    @RequestMapping("/update")
    public Object update(PersonnelUser personnelUser) {
        ModelMap map = new ModelMap();
        String year = CalendarUtil.getYear();
        String month = CalendarUtil.getMonth();
        String quarter = CalendarUtil.getQuarter(month);
        int i = Integer.parseInt(quarter) - 1;
        if (i == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "4";
        } else {
            month = String.valueOf(i);
        }
        PersonnelScorring scorring = new PersonnelScorring();
        scorring.setMonth(month);
        scorring.setYear(year);
        PersonnelUser personnelUserById = personnelUserService.selectById(personnelUser.getId());
        User user = userService.selectByMoneyCard(personnelUser.getMoneycard());
        PersonnelUser personnelUserByMoneycard = personnelUserService.selectByPrimaryKey(personnelUser.getMoneycard());
        scorring.setMoneycard(personnelUserById.getMoneycard());
        if (personnelUserByMoneycard != null) {
            if (!personnelUserByMoneycard.getMoneycard().equals(personnelUserById.getMoneycard())) {
                String moneycard = personnelUserByMoneycard.getMoneycard();
                String username = personnelUserByMoneycard.getUsername();
                map.put("msg", "你修改的用户(" + moneycard + "-" + username + ")已存在");
                map.put("code", 1);
            } else {
                PersonnelScorring personnelScorring = personnelScorringService.selectOne(scorring);
                if (personnelScorring == null) {
                    //修改授权表人员信息
                    updateAuthorizationUser(personnelUser, map);
                } else {
                    map.put("msg", "您修改的用户在" + personnelScorring.getDepartmentname() + "已经打过分,请确认是否继续修改");
                    map.put("code", 1);
                }
            }
        } else {
            if (user != null) {
                //判断是否是打分用户
                List<UserRoleKey> roleList = userRoleService.selectUserRoleByUserCode(user.getUsercode());
                if (roleList.size() > 0) {
                    for (UserRoleKey role : roleList) {
                        if (role.getRolecode().equals("150") || role.getRolecode().equals("500") || role.getRolecode().equals("400") || role.getRolecode().equals("700")) {
                            //修改授权表人员信息
                            updateAuthorizationUser(personnelUser, map);
                            break;
                        } else {
                            map.put("msg", "您修改的用户是组织部成员但不是组织部打分用户,请检查后重新修改");
                            map.put("code", 1);
                        }
                    }
                }
            } else {
                //修改授权表人员信息
                updateAuthorizationUser(personnelUser, map);
            }
        }
        return map;

    }

    private void updateAuthorizationUser(PersonnelUser personnelUser, ModelMap map) {
        int count = personnelUserService.updateByPrimaryKeySelective(personnelUser);
        AuthorizationUser authorizationUser = authorizationUserService.selectByMoneyCard(personnelUser.getMoneycard());
        judgeAuthorizationUserIsNull(personnelUser, authorizationUser);
        Rater rater = raterService.selectByMoneyCard(personnelUser.getMoneycard());
        //修改管理用户信息
        updateRaterInfo(personnelUser, rater);
        //修改授权日志表信息
        updatePersonnelAuthorization(personnelUser);
        getMsg(map, count);
    }

    /**
     * 确认修改人员信息
     *
     * @param personnelUser
     * @return
     */
    @RequestMapping("/confirmUpdate")
    public Object confirmUpdate(PersonnelUser personnelUser) {
        ModelMap map = new ModelMap();
        try {
            String year = CalendarUtil.getYear();
            String month = CalendarUtil.getMonth();
            String quarter = CalendarUtil.getQuarter(month);
            int i = Integer.parseInt(quarter) - 1;
            if (i == 0) {
                int lastYear = Integer.parseInt(year) - 1;
                year = String.valueOf(lastYear);
                month = "4";
            } else {
                month = String.valueOf(i);
            }
            PersonnelScorring scorring = new PersonnelScorring();
            scorring.setMoneycard(personnelUser.getMoneycard());
            scorring.setYear(year);
            scorring.setMonth(month);
            personnelScorringService.deleteOne(scorring);
            personnelUserService.updateByPrimaryKeySelective(personnelUser);
           //修改授权表人员信息
            AuthorizationUser authorizationUser = authorizationUserService.selectByMoneyCard(personnelUser.getMoneycard());
            judgeAuthorizationUserIsNull(personnelUser, authorizationUser);
            //修改管理用户信息
            Rater rater = raterService.selectByMoneyCard(personnelUser.getMoneycard());
            updateRaterInfo(personnelUser, rater);
            //修改授权日志表信息
            updatePersonnelAuthorization(personnelUser);
            map.put("msg", "修改成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("msg", "修改失败");
            map.put("code", 1);
        }
        return map;
    }

    private void updatePersonnelAuthorization(PersonnelUser personnelUser) {
        PersonnelAuthorization agent = personnelAuthorizationService.findByAgent(personnelUser.getMoneycard());
        if (agent != null) {
            agent.setDepartmentname(personnelUser.getDepartmentname());
            agent.setAgentusername(personnelUser.getUsername());
            personnelAuthorizationService.updateByPrimaryKeySelective(agent);
        }
    }

    private void updateRaterInfo(PersonnelUser personnelUser, Rater rater) {
        if (rater != null) {
            rater.setDepartment(personnelUser.getDepartmentname());
            rater.setScorringname(personnelUser.getUsername());
            rater.setScorringcode(personnelUser.getMoneycard());
            raterService.updateByPrimaryKeySelective(rater);
        }
    }

    private void judgeAuthorizationUserIsNull(PersonnelUser personnelUser, AuthorizationUser authorizationUser) {
        if (authorizationUser != null) {
            authorizationUser.setDepartmentname(personnelUser.getDepartmentname());
            authorizationUser.setUsername(personnelUser.getUsername());
            authorizationUser.setMoneycard(personnelUser.getMoneycard());
            authorizationUserService.updateByPrimaryKeySelective(authorizationUser);
        }
    }


    private void getMsg(ModelMap map, int count) {
        if (count > 0) {
            map.put("msg", "修改用户成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改用户失败");
            map.put("code", 2);
        }
    }

    /**
     * 删除用户数据
     *
     * @param moneycard
     * @return
     */
    @RequestMapping("/delete")
    public Object delete(String moneycard) {
        ModelMap map = new ModelMap();
        try {
            personnelUserService.deleteByPrimaryKey(moneycard);
           //删除授权表中的人事部人员
            AuthorizationUser authorizationUser = authorizationUserService.selectByMoneyCard(moneycard);
            if (authorizationUser!=null) {
                authorizationUserService.deleteByPrimaryKey(authorizationUser.getId());
            }
            //删除当前季度的评分
            deleteCurrentPersonnelScorring(moneycard);
            Rater rater = raterService.selectByMoneyCard(moneycard);
            if (rater!=null){
                raterService.deleteRaterByScorringCode(rater.getScorringcode());
                User user = userService.selectByMoneyCard(moneycard);
                if (user!=null) {
                    List<UserRoleKey> keys = userRoleService.selectUserRoleByUserCode(user.getUsercode());
                    if (keys.size() > 0) {
                        for (UserRoleKey roleKey : keys) {
                            if (roleKey.getRolecode().equals("500")) {
                                userRoleService.deleteByPrimaryKey(roleKey);
                            }
                        }
                    }
                    userService.deleteUserByMoneyCard(moneycard);
                }
                //删除历史授权表信息
                PersonnelAuthorization agent = personnelAuthorizationService.findByAgent(moneycard);
                if (agent!=null){
                    personnelAuthorizationService.deleteByPrimaryKey(agent.getId());
                }
            }
            map.put("msg", "删除用户成功");
            map.put("code", 0);
        } catch (Exception e) {
            map.put("error", e.getMessage());
            map.put("msg", "删除用户失败");
            map.put("code", 1);
        }
        return map;

    }

    private void deleteCurrentPersonnelScorring(String moneycard) {
        String year = CalendarUtil.getYear();
        String month = CalendarUtil.getMonth();
        String quarter = CalendarUtil.getQuarter(month);
        int i = Integer.parseInt(quarter) - 1;
        if (i == 0) {
            int lastYear = Integer.parseInt(year) - 1;
            year = String.valueOf(lastYear);
            month = "4";
        } else {
            month = String.valueOf(i);
        }
        PersonnelScorring scorring = new PersonnelScorring();
        scorring.setMoneycard(moneycard);
        scorring.setYear(year);
        scorring.setMonth(month);
        PersonnelScorring selectOne = personnelScorringService.selectOne(scorring);
        if (selectOne!=null){
            personnelScorringService.deleteByPrimaryKey(selectOne.getUsercode());
        }
    }

    /**
     * 查询部门下的所有员工
     *
     * @param personnelUser
     * @param loginmoneycard
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/getDeptUser")
    public Object getDeptUser(PersonnelUser personnelUser, String loginmoneycard, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        try {
            Rater rater = raterService.selectByMoneyCard(loginmoneycard);
            PageHelper.startPage(pageNum, pageSize);
            personnelUser.setDepartmentname(rater.getDepartment());
            List<PersonnelUser> list = personnelUserService.selectListByPersonner(personnelUser);
            PageInfo<PersonnelUser> pageInfo = new PageInfo<>(list);
            if (list.size() > 0) {
                map.put("msg", "查询成功");
                map.put("totalPages", pageInfo.getTotal());
                map.put("data", list);
                map.put("code", 0);
            } else {
                map.put("msg", "该部门下暂无用户，请联系管理员");
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 导出人员信息excel
     *
     * @param info
     * @return
     */
    @RequestMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response, String info, PersonnelUser user) {
        List<PersonnelUser> list;
        String title = "行政后勤工作人员表";
        //定义表的列名
        String[] rowsName = new String[]{"序号", "发薪号", "用户姓名", "所属部门"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<>();
        Object[] objs;
        //json字符串转对象
        JSONObject jsonObject = JSONObject.fromObject(info);
        PersonnelUserInfo userInfo = (PersonnelUserInfo) JSONObject.toBean(jsonObject, PersonnelUserInfo.class);
        user.setFlag(userInfo.getFlag());
        user.setDepartmentname(userInfo.getDepartmentname());
        user.setUsername(userInfo.getUsername());
        list = personnelUserService.selectAllPersonnelByLike(user);
        for (int i = 0; i < list.size(); i++) {
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = list.get(i).getMoneycard();
            objs[2] = list.get(i).getUsername();
            objs[3] = list.get(i).getDepartmentname();
            dataList.add(objs);
        }
        try {
            ExcelUtil excelUtil = new ExcelUtil();
            String fileName = new String("行政后勤工作人员表.xls".getBytes("UTF-8"), "iso-8859-1");    //生成word文件的文件名
            excelUtil.exportExcel(title, rowsName, dataList, fileName, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
