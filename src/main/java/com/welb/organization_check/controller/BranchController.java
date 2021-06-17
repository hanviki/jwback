package com.welb.organization_check.controller;

import com.welb.organization_check.entity.Branch;
import com.welb.organization_check.service.IBranchService;
import com.welb.util.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: BranchController
 * @projectName kao
 * @description: 支部管理控制器
 * @date 2019/5/2015:06
 */
@RestController
@RequestMapping("/branch")
public class BranchController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IBranchService branchService;

    /**
     * 查询所有部支部 包括模糊匹配查询
     *
     * @param branch
     * @return
     */
    @RequestMapping(value = "/treelist", produces = "application/json;charset=utf-8")
    public Object selectAll(HttpServletRequest req, Branch branch) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            List<Branch> brancheList = new ArrayList<>();
            try {
                List<Branch> branches = getBranchList(branch);
                for (Branch branch1 : branches
                ) {
                    if (branch1.getUpbranchcode().equals("0")) {
                        brancheList.add(branch1);
                    }
                }
                map.put("msg", "查询支部成功");
                map.put("data", brancheList);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询支部失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 递归查询支部
     *
     * @param branch
     * @return
     */
    private List<Branch> getBranchList(Branch branch) {
        List<Branch> branches = branchService.selectBranchTrees(branch);
        if (branches.size() > 0) {
            Branch branch1 = new Branch();
            for (int i = 0; i < branches.size(); i++) {
                branch1.setUpbranchcode(branches.get(i).getBranchcode());
                List<Branch> branchList = getBranchList(branch1);
                if (branchList.size() > 0) {
                    branches.get(i).setChildBranch(branchList);
                } else {
                    branches.get(i).setChildBranch(null);
                }
            }
        }
        return branches;
    }

    /**
     * 添加支部
     *
     * @param branch
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object AddBranch(Branch branch) {
        ModelMap map = new ModelMap();
        //如果支部编号不为空，则添加子部门
        if (branch.getBranchcode() != null) {
            branch.setUpbranchcode(branch.getBranchcode());
        }
        if (branch.getUpbranchcode() == null || branch.getUpbranchcode().equals("")) {
            branch.setUpbranchcode("0");
        }
        //添加新的部门
        int count = branchService.insertSelective(branch);
        if (count > 0) {
            map.put("msg", "添加支部成功");
            map.put("code", 0);
        } else {
            map.put("msg", "添加支部失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 修改支部
     *
     * @param branch
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateBranch(Branch branch) {
        ModelMap map = new ModelMap();
        int count = branchService.updateByPrimaryKeySelective(branch);
        if (count > 0) {
            map.put("msg", "修改支部成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改支部失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 通过branchcode删除支部
     *
     * @param branchcode
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteBranch(String branchcode) {
        ModelMap map = new ModelMap();
        int count = branchService.deleteByPrimaryKey(branchcode);
        if (count > 0) {
            map.put("msg", "删除支部成功");
            map.put("code", 0);
        } else {
            map.put("msg", "删除支部失败");
            map.put("code", 1);
        }
        return map;
    }

}
