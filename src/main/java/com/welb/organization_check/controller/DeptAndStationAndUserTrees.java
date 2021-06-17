package com.welb.organization_check.controller;

import com.welb.organization_check.entity.Department;
import com.welb.organization_check.entity.Station;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.service.IDepartmentService;
import com.welb.organization_check.service.IStationService;
import com.welb.organization_check.service.IUserService;
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
 * @title: DeptAndStationAndUserTrees
 * @projectName xh-360appraisal-interface
 * @description: 级联查询部门下的岗位下的员工
 * @date 2019/5/2416:14
 */
@RestController
@RequestMapping("usertree")
public class DeptAndStationAndUserTrees {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IDepartmentService departmentService;
    @Resource
    IStationService stationService;
    @Resource
    IUserService userService;

    @RequestMapping(value = "treelist", produces = "application/json;charset=utf-8")
    public Object SelectAll(Department department) {
        ModelMap map = new ModelMap();
        List<Department> departments;
        List<Department> departments1 = new ArrayList<>();
        try {
            departments = getDeptAndStationAndUserTrees(department);
            for (Department department1 : departments) {
                if (department1.getUpdepartmentcode().equals("0")) {
                    department1.setCommoncode(department1.getDepartmentcode());
                    department1.setCommonname(department1.getDepartmentname());
                    departments1.add(department1);
                }
            }
            map.put("msg", "级联查询用户成功");
            map.put("data", departments1);
            map.put("code", 0);
        } catch (Exception e) {
            log.error(LogUtil.getTrace(e));
            map.put("msg", "级联查询用户失败");
            map.put("code", 1);
        }
        return map;
    }

    private List<Department> getDeptAndStationAndUserTrees(Department department) {
        //查找所有部门
        List<Department> departments = departmentService.selectDepartmentTrees(department);
        //新建一个部门集合
        List<Department> departmentList = new ArrayList<>();
        //新建一个部门对象
        Department department1 = new Department();
        //判断部门集合是否为空，不为空则递归部门
        if (departments.size() > 0) {
            for (int i = 0; i < departments.size(); i++) {
                //递归子部门
                department1.setUpdepartmentcode(departments.get(i).getDepartmentcode());
                List<Department> trees = getDeptAndStationAndUserTrees(department1);
                List<Department> treeList = new ArrayList<>();
                if (trees.size() > 0) {
                    for (Department dept : trees) {
                        //满足前端需要
                        dept.setCommoncode(dept.getDepartmentcode());
                        dept.setCommonname(dept.getDepartmentname());
                        treeList.add(dept);
                    }
                    departments.get(i).setChildDept(treeList);
                    if (departments.get(i).getChildDept().size() == 0) {
                        departments.get(i).setChildDept(null);
                    }

                    departmentList.add(departments.get(i));
                } else {
                    departments.get(i).setChildDept(null);
                    List<Station> stationList = new ArrayList<>();
                    List<Station> stations = stationService.selectStationsByDeptCode(departments.get(i).getDepartmentcode());
                    if (stations.size() > 0) {
                        //查找所有用户
                        for (int j = 0; j < stations.size(); j++) {
                            //满足前端的要求
                            stations.get(j).setCommoncode(stations.get(j).getStationcode());
                            stations.get(j).setCommonname(stations.get(j).getStationname());
                            //查找所有用户
                            List<User> users = userService.selectUserByStationCode(stations.get(j).getStationcode());
                            if (users.size() > 0) {
                                for (User user : users) {
                                    //满足前端的需要
                                    user.setCommoncode(user.getUsercode());
                                    user.setCommonname(user.getUsername());
                                }
                                stations.get(j).setUserTree(users);
                                stationList.add(stations.get(j));
                            }
                            departments.get(i).setStations(stationList);
                        }
                        departmentList.add(departments.get(i));
                    }
                }
            }
        }
        return departmentList;
    }
}
