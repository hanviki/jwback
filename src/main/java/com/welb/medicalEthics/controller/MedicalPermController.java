package com.welb.medicalEthics.controller;

import com.welb.medicalEthics.dto.MedicalPermDto;
import com.welb.medicalEthics.entity.MedicalEthicsMsg;
import com.welb.medicalEthics.entity.MedicalEthicsUserRole;
import com.welb.medicalEthics.service.MedicalPermService;
import com.welb.sysBase.util.BaseController;
import com.welb.sysBase.util.PageUtils;
import com.welb.util.LogUtil;
import com.welb.util.PageData;
import com.welb.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 医德考评权限相关
 * @author: luox
 * @date： 2021/2/1
 */

@RestController
@RequestMapping("/medicalPerm")
public class MedicalPermController extends BaseController {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private MedicalPermService medicalPermService;

    /**
     * listUser 查询所有的普通管理员和超级管理员
     *
     * @return {@link List<PageData>}
     *
     */
    @RequestMapping("/list")
    public Object listUser(){
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            List<MedicalPermDto> dataSource = medicalPermService.listAdmin(param);
            List<MedicalPermDto> listData =  PageUtils.PaginationSettingForPages(param, dataSource);
            map.put("totalPages", dataSource.size());
            map.put("data", listData);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping("/update")
    public Object update(MedicalPermDto dto){
        ModelMap map = new ModelMap();
        try {
            medicalPermService.updateUserRole(dto);
            map.put("msg", "更新成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "更新失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping("/delete")
    public Object deleteUserRole(){
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        try {
            String userId = param.get("userId");
            if(StringUtils.isNotEmpty(userId)){
                medicalPermService.deleteAdminRole(userId);
                map.put("msg", "删除成功");
                map.put("code", 0);
            }else{
                map.put("msg", "错误的请求参数");
                map.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "更新失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * insertUserRole
     *
     * @return {@link Object}
     *
     */
    @RequestMapping("/insert")
    public Object insertUserRole(){
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        try {
            String userId = param.get("userId");
            String roleId = param.get("roleId");
            String status = param.get("status");
            if(StringUtils.isNotEmpty(userId) || StringUtils.isNotEmpty(roleId)){
                List<MedicalEthicsUserRole> list = medicalPermService.selectBaseByUserId(userId);
                //flag:数据是否存在 ,为false可以添加
                boolean flag = true;
                if(list.isEmpty()){
                    flag = false;
                }else{
                    for(MedicalEthicsUserRole ur : list){
                        //存在管理员或者超级管理员
                        if(ur.getRoleId() == 1 || ur.getRoleId() == 8){
                            flag = true;
                            break;
                        }
                        flag = false;
                    }
                }
                if(!flag){
                    MedicalPermDto dto = new MedicalPermDto();
                    dto.setRoleId(roleId);
                    dto.setUserId(userId);
                    dto.setStatus(status);
                    map.put("msg", "操作成功");
                    map.put("code", 0);
                    medicalPermService.insertUserRole(dto);
                }else{
                    map.put("msg", "该用户已是管理员");
                    map.put("code", 1);
                }
            }else{
                map.put("msg", "错误的请求参数");
                map.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "更新失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping("/searchUser")
    public Object listAllUser(){
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        String key = param.get("key");
        try {
            if(StringUtils.isNotEmpty(key)){
                List<MedicalEthicsMsg> dataSource = medicalPermService.selectUserByKey(key);
                param.put("pageNum","1");
                param.put("pageSize","10");
                List<MedicalEthicsMsg> listData =  PageUtils.PaginationSettingForPages(param, dataSource);
                map.put("msg", "操作成功");
                map.put("code", 0);
                map.put("data", listData);
                map.put("totalPages", dataSource.size());

            }else{
                map.put("msg", "错误的请求参数");
                map.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }


}
