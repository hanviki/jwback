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
import java.util.ArrayList;
import java.util.List;

/**
 * @author luoyaozu
 * @title: DeptAndStationTrees
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/5/2222:28
 */
@RestController
@RequestMapping("/tree")
public class DeptAndStationTrees {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IDepartmentService departmentService;
    @Resource
    IStationService stationService;

    /**
     * 查询所有部门及部门下的岗位
     *
     * @return
     */
    @RequestMapping(value = "/treelist", produces = "application/json;charset=utf-8")
    public Object selectAll(Department department) {
        ModelMap map = new ModelMap();
        List<Department> departmentDtos;
        List<Department> departments = new ArrayList<>();
        try {
            departmentDtos = getDepartmentAndStationList(department);
            for (Department dept : departmentDtos) {
                if (dept.getUpdepartmentcode().equals("0")) {
                    departments.add(dept);
                }
            }
            map.put("msg", "级联查询成功");
            map.put("data", departments);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "级联查询失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 递归查询部门及岗位  级联查询
     *
     * @param department
     * @return
     */
    private List<Department> getDepartmentAndStationList(Department department) {
        //获取所有部门
        List<Department> departments = departmentService.selectDepartmentTrees(department);
        List<Department> departmentList = new ArrayList<>();
        if (departments.size() > 0) {
            Department department1 = new Department();
            for (int i = 0; i < departments.size(); i++) {
                department1.setUpdepartmentcode(departments.get(i).getDepartmentcode());
                //递归
                List<Department> dtos = getDepartmentAndStationList(department1);
                if (dtos.size() > 0) {
                    departments.get(i).setChildDept(dtos);
                    departmentList.add(departments.get(i));
                } else {
                    departments.get(i).setChildDept(null);
                    //查询该部门下所有的岗位
                    List<Station> stationList = stationService.selectStationsByDeptCode(departments.get(i).getDepartmentcode());
                    if (stationList.size() > 0) {
                        departments.get(i).setStations(stationList);
                        for (int j = 0; j < stationList.size(); j++) {
                            stationList.get(j).setDepartmentname(stationList.get(j).getStationname());
                            //满足前端的要求
                            stationList.get(j).setDepartmentcode(stationList.get(j).getStationcode());
                        }
                        departmentList.add(departments.get(i));
                    } else {
                        departments.get(i).setStations(null);
                    }
                }

            }
        }
        return departmentList;
    }


}
