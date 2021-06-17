package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.entity.MedicalEthicsUser;
import com.welb.medicalEthics.service.MedicalEthicsUserService;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.PageUtils;
import com.welb.util.LogUtil;
import com.welb.util.Tools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/medicalEthicsUser")
public class MedicalEthicsUserController extends BaseController {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private MedicalEthicsUserService service;

    @Autowired
    private IUserService userService;

    /**
     * 获取当前登录用户的roleList
     * @return
     */
    @RequestMapping("/showSingleRole")
    public Object showSingleRole() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            User user = userService.getUserByUserCode(param.get("u_id"));
            List<MedicalEthicsUser> usersList = service.list(new HashMap<String, String>() {{
                put("userId", user.getMoneycard());
            }});
            if (usersList != null && !usersList.isEmpty()) {
                List<String> roleList= new ArrayList<>();
                usersList.forEach(u ->{
                    roleList.add(u.getRoleCode());
                });
                usersList.get(0).setRoleList(roleList);
                map.put("data", usersList.get(0));
                map.put("msg", "用户查询成功");
                map.put("code", 0);
            } else {
                map.put("msg", "用户查询异常");
                map.put("code", 0);
            }
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "用户查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * listRoles 考核领导列表
     *
     * @return {@link Object}
     */
    @RequestMapping("/listRoles")
    public Object listRoles() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            List<MedicalEthicsUser> dataSource = service.listRoles(new HashMap<>(param));
            List<MedicalEthicsUser> listData = PageUtils.PaginationSettingForPages(param, dataSource);
            map.put("totalPages", dataSource.size());
            map.put("data", listData);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "用户查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * list 获取所有的审批用户列表
     * 根据查询参数分类 roleCode-101 党支部数据
     *
     * @return {@link Object}
     */
    @RequestMapping("/list")
    public Object list() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            Map<String, String> pageParam = new HashMap<String, String>(){{put("pageNum", param.get("pageNum")); put("pageSize", param.get("pageSize"));}};
            param.remove("pageNum");
            param.remove("pageSize");
            List<MedicalEthicsUser> dataSource = service.list(new HashMap<>(param));
            List<MedicalEthicsUser> returnList = PageUtils.PaginationSettingForPages(pageParam, dataSource);
            map.put("totalPages", dataSource.size());
            map.put("data", returnList);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "用户查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

}
