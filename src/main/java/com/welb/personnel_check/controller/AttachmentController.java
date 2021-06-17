package com.welb.personnel_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.*;
import com.welb.organization_check.service.*;
import com.welb.personnel_check.entity.Attachment;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.personnel_check.entity.Rater;
import com.welb.personnel_check.service.IAttachmentService;
import com.welb.personnel_check.service.IPersonnelUserService;
import com.welb.personnel_check.service.IRaterService;
import com.welb.util.CalendarUtil;
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
 * @title: AttachmentController
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/8/1310:02
 */
@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IAttachmentService attachmentService;
    @Resource
    IUserService userService;
    @Resource
    IRaterService raterService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IPersonnelUserService personnelUserService;

    /**
     * 上传负责人签字的分数pdf
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
        String path1 = path.substring(0, path.lastIndexOf("360kao"));
        //获取路径
        String realPath = path1 + "360check" + File.separator + "picture" + File.separator + "file";
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
            e.printStackTrace();
            log.error(LogUtil.getTrace(e));
            map.put("msg", "上传失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 确认上传分数pdf
     *
     * @param req
     * @param attachment
     * @return
     */
    @RequestMapping("/confirmUpload")
    public Object confirmUpload(HttpServletRequest req, Attachment attachment) {
        ModelMap map = new ModelMap();
        String year;
        String month;
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

                } else {
                    for (UserRoleKey roleKey : role) {
                        if (roleKey.getRolecode().equals("400")) {
                            flag = true;
                        }
                    }
                }
                if (attachment.getYear().equals("") && attachment.getMonth().equals("")) {//初始化默认的是当前年份的上一季度
                    year = CalendarUtil.getYear();
                    month = CalendarUtil.getMonth();
                    String quarter = CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(quarter) - 1;
                    if (count == 0) {
                        int lastyear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastyear);
                        month = "4";
                        attachment.setYear(year);
                        attachment.setMonth(month);
                        judgeAddAndUpdate(attachment, map, year, month, flag, user.getMoneycard(), rater);
                    } else {
                        month = String.valueOf(count);
                        attachment.setYear(year);
                        attachment.setMonth(month);
                        judgeAddAndUpdate(attachment, map, year, month, flag, user.getMoneycard(), rater);
                    }
                } else if (attachment.getYear().equals("") && !attachment.getMonth().equals("") || !attachment.getYear().equals("") && attachment.getMonth().equals("")) {
                    map.put("msg", "年份和季度必须一起选择");
                    map.put("code", 1);
                } else {
                    judgeAddAndUpdate(attachment, map, attachment.getYear(), attachment.getMonth(), flag, user.getMoneycard(), rater);
                }
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;
    }

    private void judgeAddAndUpdate(Attachment attachment, ModelMap map, String year, String month, boolean flag, String moneycard, Rater rater) {
        Attachment attach;
        if (rater != null) {//人事部用户管理存在该用户
            if (flag == true) {//超级管理员
                attachment.setDepart(rater.getDepartment());
                attach = attachmentService.selectAttachmentByMoneyCard(year, month, moneycard);
            } else {//部门长
                attachment.setMoneycard(rater.getScorringcode());
                attachment.setDepart(rater.getDepartment());
                attach = attachmentService.selectAttachmentByDepart(year, month, attachment.getDepart());
            }
            judgeAddAndUpdateAttachment(attachment, map, attach);
        } else {//人事部用户管理不存在该用户
            if (flag == true) {
                attach = attachmentService.selectAttachmentByMoneyCard(year, month, moneycard);
                judgeAddAndUpdateAttachment(attachment, map, attach);
            } else {
                map.put("msg", "请先在用户管理中添加该用户的相关信息");
                map.put("code", 1);
            }
        }

    }

    private void judgeAddAndUpdateAttachment(Attachment attachment, ModelMap map, Attachment attach) {
        if (attach == null) {
            attachmentService.insertSelective(attachment);
        } else {
            attachment.setAttachmentcode(attach.getAttachmentcode());
            attachmentService.updateByPrimaryKeySelective(attachment);
        }
        map.put("filepath", attachment.getFilepath());
        map.put("filename", attachment.getFilename());
        map.put("msg", "提交成功");
        map.put("code", 0);
    }


    /**
     * 非人事部管理员确认上传分数pdf
     *
     * @param attachment
     * @return
     */
    @RequestMapping("/confirmUploadByManager")
    public Object confirmUploadByManager(HttpServletRequest req, Attachment attachment) {
        ModelMap map = new ModelMap();
        String year;
        String month;
        try {
            if (attachment.getDepart().equals("")) {
                map.put("msg", "请先选择部门");
                map.put("code", 1);
            } else {
                String usercode = (String) req.getSession().getAttribute("usercode");
                User user = userService.findUserByOne(usercode);
                if (attachment.getYear().equals("") && attachment.getMonth().equals("")) {//初始化默认的是当前年份的上一季度
                    year = CalendarUtil.getYear();
                    month = CalendarUtil.getMonth();
                    String quarter = CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(quarter) - 1;
                    if (count == 0) {
                        int lastyear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastyear);
                        month = "4";
                        attachment.setYear(year);
                        attachment.setMonth(month);
                        attachment.setMoneycard(user.getMoneycard());
                        Attachment attach = attachmentService.selectAttachmentByDepart(year, month, attachment.getDepart());
                        judgeAddAndUpdateAttachment(attachment, map, attach);
                    } else {
                        month = String.valueOf(count);
                        attachment.setYear(year);
                        attachment.setMonth(month);
                        attachment.setMoneycard(user.getMoneycard());
                        Attachment attach = attachmentService.selectAttachmentByDepart(year, month, attachment.getDepart());
                        judgeAddAndUpdateAttachment(attachment, map, attach);
                    }
                } else if (attachment.getYear().equals("") && !attachment.getMonth().equals("") || !attachment.getYear().equals("") && attachment.getMonth().equals("")) {
                    map.put("msg", "年份和季度必须一起选择");
                    map.put("code", 1);
                } else {
                    year = attachment.getYear();
                    month = attachment.getMonth();
                    attachment.setMoneycard(user.getMoneycard());
                    Attachment attach = attachmentService.selectAttachmentByDepart(year, month, attachment.getDepart());
                    judgeAddAndUpdateAttachment(attachment, map, attach);
                }
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
     * 通过部门在线预览分数pdf
     * @param attachment
     * @return
     */
    @RequestMapping("/getAttachByDepart")
    public Object getAttachByDepart(Attachment attachment){
        ModelMap map=new ModelMap();
        try {
            if (attachment.getDepart().equals("")){
                map.put("msg", "请先选择部门");
                map.put("code", 1);
            }else {
                String year;
                String month;
                if (attachment.getYear().equals("") && attachment.getMonth().equals("")) {//初始化默认的是当前年份的上一季度
                    year = CalendarUtil.getYear();
                    month = CalendarUtil.getMonth();
                    String quarter = CalendarUtil.getQuarter(month);
                    int count = Integer.parseInt(quarter) - 1;
                    if (count == 0) {
                        int lastyear = Integer.parseInt(year) - 1;
                        year = String.valueOf(lastyear);
                        month = "4";
                        attachment.setYear(year);
                        attachment.setMonth(month);
                        getFilePath(attachment, map, year, month);

                    } else {
                        month = String.valueOf(count);
                        attachment.setYear(year);
                        attachment.setMonth(month);
                        getFilePath(attachment, map, year, month);

                    }
                } else if (attachment.getYear().equals("") && !attachment.getMonth().equals("") || !attachment.getYear().equals("") && attachment.getMonth().equals("")) {
                    map.put("msg", "年份和季度必须一起选择");
                    map.put("code", 1);
                } else {
                    year = attachment.getYear();
                    month = attachment.getMonth();
                    getFilePath(attachment, map, year, month);

                }
            }

        }catch (Exception e){
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return map;
    }

    private void getFilePath(Attachment attachment, ModelMap map, String year, String month) {
        Attachment attach = attachmentService.selectAttachmentByDepart(year, month, attachment.getDepart());
        if (attach != null) {
            map.put("msg", "操作成功");
            map.put("filepath", attach.getFilepath());
            map.put("filename", attach.getFilename());
            map.put("code", 0);
        } else {
            map.put("msg", "请先上传负责人签字的分数PDF！");
            map.put("code", 1);
        }
    }


    @RequestMapping("/getAttachmentList")
    public Object getAttachmentList(HttpServletRequest req,Attachment attachment,int pageNum,int pageSize){
        ModelMap map=new ModelMap();
        String  usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode!=null) {
            try {
                PageHelper.startPage(pageNum, pageSize);
                List<Attachment> attachmentList = attachmentService.getAttachmentList(attachment);
                for (Attachment list:attachmentList){
                    if (list.getUsername()==null||list.getUsername().equals("")){
                        PersonnelUser user = personnelUserService.selectByPrimaryKey(list.getMoneycard());
                        if (user!=null) {
                            list.setUsername(user.getUsername());
                        }else {
                            list.setUsername("");
                        }
                    }
                }
                PageInfo<Attachment>pageInfo=new PageInfo<>(attachmentList);
                map.put("msg", "查询成功");
                map.put("totalPages", pageInfo.getTotal());
                map.put("data",attachmentList);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询失败");
                map.put("error", e.getMessage());
                map.put("code", 1);
            }
        }else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }
}
