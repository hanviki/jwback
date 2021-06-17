package com.welb.personnel_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.personnel_check.entity.DeptCheck;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.personnel_check.entity.Rater;
import com.welb.personnel_check.service.IDeptCheckService;
import com.welb.personnel_check.service.IPersonnelUserService;
import com.welb.personnel_check.service.IRaterService;
import com.welb.util.DateUtil;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DeptCheckController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/1316:47
 */
@RestController
@RequestMapping("/deptCheck")
public class DeptCheckController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IDeptCheckService deptCheckService;
    @Resource
    IUserService userService;
    @Resource
    IRaterService raterService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IPersonnelUserService personnelUserService;

    /**
     * 上传负责人,分管院长签字的分数pdf
     *
     * @param req
     * @param file
     * @return
     */
    @RequestMapping("/upload")
    public Object uploadPdf(HttpServletRequest req, @RequestParam("file") MultipartFile file) {
        ModelMap map = new ModelMap();
        //获取YYYYMMDD格式的日期
        String days = DateUtil.getDays();
        //获取yyyyMMddHHmmss格式的日期
        String times = DateUtil.getSdfTimes();
        String path = req.getSession().getServletContext().getRealPath("/");
        //String path1 = path.substring(0, path.lastIndexOf("360kao"));
        //获取路径
        String realPath = path+"360check" + File.separator + "picture" + File.separator + "file";
        try {
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
            String filepath = newfile.getPath().replaceAll("\\\\", "/");
            int i = filepath.lastIndexOf("360check");
            filepath = filepath.substring(i, filepath.length());
            if (filename.contains(".pdf")) {
                map.put("filepath", filepath);
                map.put("filename", filename);
                map.put("msg", "上传成功");
                map.put("code", 0);
            } else {
                map.put("msg", "不支持该格式上传，请上传pdf格式的图片");
                map.put("code", 1);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "上传失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 确认上传部门考核办法
     *
     * @param req
     * @param deptCheck
     * @return
     */
    @RequestMapping("/confirmUpload")
    public Object confirmUpload(HttpServletRequest req, DeptCheck deptCheck) {
        ModelMap map = new ModelMap();
        try {
            String usercode = (String) req.getSession().getAttribute("usercode");
            List<UserRoleKey> role = userRoleService.selectUserRoleByUserCode(usercode);
            boolean flag = false;
            if (usercode != null) {
                User user = userService.findRaterUserByUserCode(usercode);
                Rater rater = raterService.selectByMoneyCard(user.getMoneycard());
                if (rater != null) {
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400") && !rater.getDepartment().equals("人事处")) {
                            flag = true;
                        }
                    }
                    if (flag == true) {
                        deptCheck.setDepart(rater.getDepartment());
                        getDeptCheck(deptCheck, map, user);
                    } else {
                        deptCheck.setMoneycard(rater.getScorringcode());
                        deptCheck.setDepart(rater.getDepartment());
                        DeptCheck check = deptCheckService.selectDeptCheckByYearAndDepart(deptCheck.getYear(), deptCheck.getDepart());
                        if (check == null) {
                            deptCheckService.insertSelective(deptCheck);
                        } else {
                            deptCheck.setDeptcheckcode(check.getDeptcheckcode());
                            deptCheckService.updateByPrimaryKeySelective(deptCheck);
                        }
                        map.put("filepath", deptCheck.getFilepath());
                        map.put("filename", deptCheck.getFilename());
                        map.put("msg", "提交成功");
                        map.put("code", 0);
                    }
                } else {
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400")) {
                            flag = true;
                        }
                    }
                    if (flag == true) {
                        getDeptCheck(deptCheck, map, user);
                    } else {
                        map.put("msg", "请先在用户管理中添加该用户的相关信息");
                        map.put("code", 1);
                    }

                }
            } else {
                map.put("msg", "登录用户过期,请重新登录");
                map.put("code", 810);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;

    }

    private void getDeptCheck(DeptCheck deptCheck, ModelMap map, User user) {
        deptCheck.setMoneycard(user.getMoneycard());
        DeptCheck check = deptCheckService.selectDeptCheckByYearAndMoneyCard(deptCheck.getYear(), deptCheck.getMoneycard());
        if (check == null) {
            deptCheckService.insertSelective(deptCheck);
        } else {
            deptCheck.setDeptcheckcode(check.getDeptcheckcode());
            deptCheckService.updateByPrimaryKeySelective(deptCheck);
        }
        map.put("filepath", deptCheck.getFilepath());
        map.put("filename", deptCheck.getFilename());
        map.put("msg", "提交成功");
        map.put("code", 0);
    }

    /**
     * 通过年份查找部门考核附件信息
     *
     * @param year
     * @return
     */
    @RequestMapping("/selectDeptCheckByYear")
    public Object selectDeptCheckByYear(HttpServletRequest req, String year) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        DeptCheck deptCheck;
        if (usercode != null) {
            try {
                User user = userService.findRaterUserByUserCode(usercode);
                Rater rater = raterService.selectByMoneyCard(user.getMoneycard());
                List<UserRoleKey> role = userRoleService.selectUserRoleByUserCode(usercode);
                boolean flag = false;
                if (rater != null) {//人事部管理用户存在
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400") && !rater.getDepartment().equals("人事处")) {
                            flag = true;
                        }
                    }
                    if (flag == true) {
                        deptCheck = null;
                    } else {
                        deptCheck = deptCheckService.selectDeptCheckByYearAndDepart(year, rater.getDepartment());
                    }
                    if (deptCheck == null) {
                        map.put("data", "");
                    } else {
                        map.put("data", deptCheck);
                    }
                    map.put("msg", "查询成功");
                    map.put("code", 0);
                } else {//人事部管理用户不存在
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400")) {
                            flag = true;
                        }
                    }
                    if (flag == true) {//超级管理员
                        map.put("data", "");
                        map.put("msg", "查询成功");
                        map.put("code", 0);
                    } else {//部门长
                        map.put("msg", "请先在用户管理中添加该用户的相关信息");
                        map.put("code", 1);
                    }
                }
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 查找所有部门
     *
     * @param deptCheck
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/getList")
    public Object getList(DeptCheck deptCheck, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<DeptCheck> list = deptCheckService.selectList(deptCheck);
            for (DeptCheck check:list){
                if (check.getUsername()==null||check.getUsername().equals("")){
                    PersonnelUser user = personnelUserService.selectByPrimaryKey(check.getMoneycard());
                    if (user!=null){
                        check.setUsername(user.getUsername());
                    }else {
                        check.setUsername("");
                    }
                }
            }
            PageInfo<DeptCheck> pageInfo = new PageInfo<>(list);
            map.put("pageTotals", pageInfo.getTotal());
            map.put("data", list);
            map.put("msg", "查询成功");
            map.put("code", 0);

        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);

        }
        return map;
    }


    /**
     * 非人事部管理员确认上传部门考核办法
     *
     * @param req
     * @param deptCheck
     * @return
     */
    @RequestMapping("/confirmUploadByManager")
    public Object confirmUploadByManager(HttpServletRequest req, DeptCheck deptCheck) {
        ModelMap map = new ModelMap();
        try {
            String usercode = (String) req.getSession().getAttribute("usercode");
            if (usercode != null) {
                if (deptCheck.getDepart().equals("")) {
                    map.put("msg", "请先选择部门");
                    map.put("code", 1);
                } else {
                    User user = userService.findUserByOne(usercode);
                    deptCheck.setMoneycard(user.getMoneycard());
                    DeptCheck check = deptCheckService.selectDeptCheckByYearAndDepart(deptCheck.getYear(), deptCheck.getDepart());
                    if (check == null) {
                        deptCheckService.insertSelective(deptCheck);
                    } else {
                        deptCheck.setDeptcheckcode(check.getDeptcheckcode());
                        deptCheckService.updateByPrimaryKeySelective(deptCheck);
                    }
                    map.put("filepath", deptCheck.getFilepath());
                    map.put("filename", deptCheck.getFilename());
                    map.put("msg", "提交成功");
                    map.put("code", 0);
                }
            } else {
                map.put("msg", "登录用户过期,请重新登录");
                map.put("code", 810);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;

    }


    /**
     * 非人事部管理员通过年份查找部门考核附件信息
     *
     * @param dept
     * @return
     */
    @RequestMapping("/selectDeptCheckByManager")
    public Object selectDeptCheckByManager(HttpServletRequest req, DeptCheck dept) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            try {
                DeptCheck deptCheck = deptCheckService.selectDeptCheckByYearAndDepart(dept.getYear(), dept.getDepart());
                if (deptCheck == null) {
                    map.put("data", "");
                } else {
                    map.put("data", deptCheck);
                }
                map.put("msg", "查询成功");
                map.put("code", 0);

        } catch(Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("code", 1);
            }
        } else{
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }


}
