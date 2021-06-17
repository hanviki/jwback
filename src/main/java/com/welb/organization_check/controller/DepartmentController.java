package com.welb.organization_check.controller;

import com.welb.organization_check.entity.Department;
import com.welb.organization_check.entity.Station;
import com.welb.organization_check.service.IDepartmentService;
import com.welb.organization_check.service.IStationService;
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
 * @title: DepartmentController
 * @projectName kao
 * @description: 用户控制类
 * @date 2019/5/1621:50
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IDepartmentService departmentService;
    @Resource
    IStationService stationService;

    /**
     * 查询所有部门 树形展示所有部门 包含模糊查詢
     *
     * @return
     */
    @RequestMapping(value = "/treelist", produces = "application/json;charset=utf-8")
    public Object selectAll(HttpServletRequest req, Department department) {
        ModelMap map = new ModelMap();
        String usercode = (String) req.getSession().getAttribute("usercode");
        if (usercode != null) {
            List<Department> departmentDtos;
            List<Department> departments = new ArrayList<>();
            try {
                departmentDtos = getDepartmentList(department);

                for (Department dept : departmentDtos) {
                    if (dept.getUpdepartmentcode().equals("0") && dept.getStations().size() == 0) {
                        departments.add(dept);
                    }
                }
                map.put("msg", "查询部门成功");
                map.put("data", departments);
                map.put("code", 0);
            } catch (Exception e) {
                log.error(LogUtil.getTrace(e));
                map.put("msg", "查询部门失败");
                map.put("code", 1);
            }
        } else {
            map.put("msg", "登录用户超时,请重新登录");
            map.put("code", 810);
        }
        return map;
    }

    /**
     * 递归查询部门
     *
     * @param department
     * @return
     */
    private List<Department> getDepartmentList(Department department) {
        List<Department> departments = departmentService.selectDepartmentTrees(department);
        if (departments.size() > 0) {
            Department department1 = new Department();
            for (int i = 0; i < departments.size(); i++) {
                department1.setUpdepartmentcode(departments.get(i).getDepartmentcode());

                List<Department> dtos = getDepartmentList(department1);
                if (dtos.size() > 0) {
                    departments.get(i).setChildDept(dtos);
                } else {
                    departments.get(i).setChildDept(null);
                }

            }
        }
        return departments;
    }


    /**
     * 添加部门
     *
     * @param department
     * @return
     */
    @RequestMapping(value = "/add", produces = "application/json;charset=utf-8")
    public Object addDepartment(Department department) {
        ModelMap map = new ModelMap();
        //若departmentcode为null,则添加新的部门，不为null,在 在当前部门下添加子部门
        if (department.getDepartmentcode() != null) {
            department.setUpdepartmentcode(department.getDepartmentcode());
        }
        if (department.getDepartmentcode() == null || department.getDepartmentcode().equals("")) {
            department.setUpdepartmentcode("0");
        }
        //添加新部门
        int count = departmentService.addDepartment(department);
        if (count > 0) {
            map.put("msg", "添加部门成功");
            map.put("code", 0);
        } else {
            map.put("msg", "添加部门失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 通过departmentcode修改部门信息
     *
     * @param department
     * @return
     */
    @RequestMapping(value = "/update", produces = "application/json;charset=utf-8")
    public Object updateDepartment(Department department) {
        ModelMap map = new ModelMap();

        int count = departmentService.updateByDeptCode(department);

        if (count > 0) {
            map.put("msg", "修改部门成功");
            map.put("code", 0);
        } else {
            map.put("msg", "修改部门失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 通过departmentcode删除部门
     *
     * @param departmentcode
     * @return
     */
    @RequestMapping(value = "/delete", produces = "application/json;charset=utf-8")
    public Object deleteDepartment(String departmentcode) {
        ModelMap map = new ModelMap();
//        删除部门，关联的岗位表中的部门应的岗位也要删除
        List<Station> stationList = stationService.selectStationsByDeptCode(departmentcode);
        //遍历删除和部门关联的岗位
        for (Station station : stationList) {
            stationService.deleteByPrimaryKey(station.getStationcode());
        }
        int count = departmentService.deleteByDeptCode(departmentcode);
        if (count > 0) {
            map.put("msg", "删除部门成功");
            map.put("code", 0);
        } else {
            map.put("msg", "删除部门失败");
            map.put("code", 1);
        }
        return map;
    }

}
