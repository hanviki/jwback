package com.welb.medicalEthics.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.welb.medicalEthics.dto.PartyBranchRelationsDto;
import com.welb.medicalEthics.entity.PartyBranchRelations;
import com.welb.medicalEthics.service.PartyBranchRelationsService;
import com.welb.sysBase.service.HUserService;
import com.welb.sysBase.util.BaseController;
import com.welb.util.StringUtil;
import com.welb.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.welb.util.LogUtil.getTrace;

/**
 * 党支部信息管理
 *
 * @author lx
 */
@RestController
@RequestMapping("/partyBranchRelations")
public class PartyBranchRelationsController extends BaseController {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private PartyBranchRelationsService service;

    @Autowired
    private HUserService personneService;

    @RequestMapping("/listPage")
    public Object list() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            Page<PartyBranchRelations> page = PageHelper.startPage(Integer.parseInt(param.get("pageNum")), Integer.parseInt(param.get("pageSize")));
            List<PartyBranchRelations> dataSource = service.list(param);
            map.put("data", dataSource);
            map.put("msg", "查询成功");
            map.put("totalPages", page.getTotal());
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * 支部人数统计分析
     * @return
     */
    @RequestMapping("/listDetail")
    public Object listDetail() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            Page<PartyBranchRelations> page = PageHelper.startPage(Integer.parseInt(param.get("pageNum")), Integer.parseInt(param.get("pageSize")));
            List<PartyBranchRelations> dataSource = service.listDetail(param);
            map.put("data", dataSource);
            map.put("msg", "查询成功");
            map.put("totalPages", page.getTotal());
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping("/list")
    public Object listByLevel() {
        ModelMap map = new ModelMap();
        try {
            Map<String, String> param = Tools.getParamMap(request);
            List<PartyBranchRelations> dataSource = service.list(param);
            map.put("totalPages", dataSource.size());
            map.put("data", dataSource);
            map.put("msg", "查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            map.put("msg", "查询失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    @RequestMapping(value = "/tree")
    @ResponseBody
    public Object tree() {
        ModelMap map = new ModelMap();
        Map<String, String> param = Tools.getParamMap(request);
        try {
            map.put("data", service.tree(param));
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            e.printStackTrace();
            map.put("error", e.getMessage());
            map.put("msg", "异常");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * treeParent 获取一二级树
     *
     * @return {@link Object}
     */
    @RequestMapping(value = "/treeParent")
    @ResponseBody
    public Object treeParent() {
        ModelMap map = new ModelMap();
        try {
            map.put("data", service.partyBranchRelations());
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            e.printStackTrace();
            map.put("error", e.getMessage());
            map.put("msg", "异常");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * branchTree 加载左侧党委树
     *
     * @return {@link Object}
     */
    @RequestMapping(value = "/branchTree")
    @ResponseBody
    public Object branchTree() {
        ModelMap map = new ModelMap();
        Map<String, String> params = Tools.getParamMap(request);
        try {
            map.put("data", service.getBranchTree(params));
            map.put("msg", "党委树查询成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "党委树查询异常");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * insert 单条新增
     *
     * @param partyBranchRelations partyBranchRelations
     * @return {@link Object}
     */
    @RequestMapping("/insert")
    public Object insert(PartyBranchRelations partyBranchRelations) {
        ModelMap map = new ModelMap();
        try {
            String leaderName = partyBranchRelations.getLeaderName();
            Integer leaderUserId = partyBranchRelations.getLeaderUserId();
            Integer level = partyBranchRelations.getLevel();
            if (level == null) {
                map.put("msg", "未获取到支部等级信息");
                map.put("code", 1);
                return ajaxJson(map);
            }
            //检查支部书记名字和发薪号是否合法
            if (StringUtil.isNotEmpty(leaderName) && null != leaderUserId) {
                if (!personneService.checkIdAndName(leaderUserId, leaderName)) {
                    map.put("msg", "用户名和发薪号不匹配");
                    map.put("code", 1);
                    return ajaxJson(map);
                }
            }
            service.insert(partyBranchRelations);
            map.put("msg", "新增成功");
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }


    /**
     * update 更新党委信息
     *
     * @param partyBranchRelations 党委信息
     * @return {@link Object}
     */
    @PostMapping("/update")
    public Object update(PartyBranchRelationsDto partyBranchRelations) {
        ModelMap map = new ModelMap();
        try {
            String leaderName = partyBranchRelations.getLeaderName();
            Integer leaderUserId = partyBranchRelations.getLeaderUserId();
            //检查支部书记名字和发薪号是否合法
            if (StringUtil.isNotEmpty(leaderName) || null != leaderUserId) {
                if (!personneService.checkIdAndName(leaderUserId, leaderName)) {
                    map.put("msg", "支部书记用户名和发薪号不匹配");
                    map.put("code", 1);
                    return ajaxJson(map);
                }
            }
            //检查打分书记的姓名和发薪号是否匹配
            String directorName = partyBranchRelations.getDirectorName();
            Integer directorUserId = partyBranchRelations.getDirectorUserId();
            if(directorUserId != null || StringUtils.isNotEmpty(directorName)){
                if(!personneService.checkIdAndName(directorUserId,directorName)){
                    map.put("msg", "打分书记用户名和发薪号不匹配");
                    map.put("code", 1);
                    return ajaxJson(map);
                }
            }

            PartyBranchRelations data = new PartyBranchRelations();
            BeanUtils.copyProperties(partyBranchRelations, data);
            service.update(data);
            map.put("msg", "更新成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

    /**
     * delete 删除单条，逻辑删除
     *
     * @param id
     * @return {@link Object}
     */

    @PostMapping("/delete/{id}")
    public Object delete(@PathVariable("id") Integer id) {
        ModelMap map = new ModelMap();
        try {
            if (!service.deleteById(id)) {
                map.put("msg", "无法删除部门，存在关联数据");
                map.put("code", 1);
                return ajaxJson(map);
            }
            map.put("msg", "操作成功");
            map.put("code", 0);
        } catch (Exception e) {
            log.error(getTrace(e));
            map.put("msg", "操作失败");
            map.put("code", 1);
        }
        return ajaxJson(map);
    }

}
