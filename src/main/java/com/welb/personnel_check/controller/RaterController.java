package com.welb.personnel_check.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.welb.organization_check.entity.Role;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.entity.UserRoleKey;
import com.welb.organization_check.service.IRoleService;
import com.welb.organization_check.service.IUserRoleService;
import com.welb.organization_check.service.IUserService;
import com.welb.personnel_check.entity.PersonnelUser;
import com.welb.personnel_check.entity.Rater;
import com.welb.personnel_check.service.IPersonnelUserService;
import com.welb.personnel_check.service.IRaterService;
import com.welb.util.LogUtil;
import com.welb.util.MD5;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.welb.sysBase.util.Constant.INITIAL_PASSWORD;

/**
 * @author luoyaozu
 * @title: RaterController
 * @projectName xh-360appraisal-interface
 * @description: 人事部评分人员控制器
 * @date 2019/9/311:02
 */
@RestController
@RequestMapping("/rater")
public class RaterController {
    private final Logger log = LogManager.getLogger(this.getClass());

    @Resource
    IRaterService raterService;
    @Resource
    IUserService userService;
    @Resource
    IUserRoleService userRoleService;
    @Resource
    IPersonnelUserService personnelUserService;
    @Resource
    IRoleService roleService;

    /**
     * 查询所有的评分人
     *
     * @param rater
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/list")
    public Object selectAllRater(Rater rater, int pageNum, int pageSize) {
        ModelMap map = new ModelMap();
        try {
            PageHelper.startPage(pageNum, pageSize);
            List<Rater> raters = raterService.selectAllRater(rater);
            PageInfo<Rater> pageInfo = new PageInfo<>(raters);
            raters = pageInfo.getList();
            map.put("totalPages", pageInfo.getTotal());
            map.put("data", raters);
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
     * 添加人事部评分人
     *
     * @param rater
     * @return
     */
    @RequestMapping("/add")
    public Object addRater(Rater rater) {
        ModelMap map = new ModelMap();
        try {
            Rater raterByCode = raterService.selectByMoneyCard(rater.getScorringcode());
            if (raterByCode != null) {
                map.put("msg", "该用户已存在");
                map.put("code", 1);
            }

            else {
                Rater rater1 = raterService.selectRaterByDept(rater.getDepartment());
                if (rater1 == null) {
                    User user = userService.selectUserByMoneyCard(rater.getScorringcode());
                    UserRoleKey userRoleKey = new UserRoleKey();
                    if (user == null) {
                        User newUser = addUser(rater);
                        userRoleKey.setUsercode(newUser.getUsercode());
                        userRoleKey.setRolecode("500");
                        userRoleService.insertSelective(userRoleKey);
                        int count = raterService.insertSelective(rater);
                        if (count > 0) {
                            map.put("msg", "添加成功,初始密码是whuh123456");
                            map.put("code", 0);
                        } else {
                            map.put("msg", "添加失败");
                            map.put("code", 1);
                        }
                    } else if (user.getUserstate().equals("0")){
                        map.put("msg", "该用户已被停用,如有疑问,请联系管理员");
                        map.put("code", 1);
                    }
                    else {
                        userRoleKey.setUsercode(user.getUsercode());
                        userRoleKey.setRolecode("500");
                        userRoleService.insertSelective(userRoleKey);
                        raterService.insertSelective(rater);
                        map.put("msg", "添加成功");
                        map.put("code", 0);

                    }
                } else {
                    map.put("msg", "该部门已存在部门长,如有疑问请联系管理员");
                    map.put("code", 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "添加失败");
            map.put("code", 1);
        }
        return map;
    }

    private User addUser(Rater rater) {
        User newUser = new User();
        newUser.setMoneycard(rater.getScorringcode());
        newUser.setUsername(rater.getScorringname());
        newUser.setPassword(MD5.md5(INITIAL_PASSWORD));//初始密码是whuh123456
        newUser.setMobile(rater.getLeaderphone());
        newUser.setRoletype("1");//角色类型属于人事部
        newUser.setFlag("0");
        newUser.setUserstate("1");
        userService.insertSelective(newUser);
        return newUser;
    }

    /**
     * 修改人事部评分人
     *
     * @param rater
     * @return
     */
    @RequestMapping("/update")
    public Object updateRater(Rater rater) {
        ModelMap map = new ModelMap();
        try {
            Rater rater1 = raterService.selectByPrimaryKey(rater.getRatercode());
            if (!rater.getScorringcode().equals(rater1.getScorringcode())) {//若修改的用户发薪号发生改变，则删除原有的管理用户信息，在新增新的用户信息

                //删除原有的用户
                delete(rater.getRatercode());
                //新增新的管理用户信息
                ModelMap map1 = (ModelMap) addRater(rater);
                if (map1.get("code").equals(1)) {
                    addRater(rater1);
                    map.put("msg",map1.get("msg"));
                    map.put("code", 1);
                }else if(map1.get("code").equals(0)){
                    map.put("msg", "修改成功");
                    map.put("code", 0);
                }else {
                    map.put("msg", "异常,请联系管理员");
                    map.put("code", 1);
                }

            } else {
                PersonnelUser personnelUser = new PersonnelUser();
                Rater depart = raterService.selectRaterByDept(rater.getDepartment());
                if (depart==null||depart.getDepartment().equals(rater1.getDepartment())) {
                    raterService.updateByPrimaryKeySelective(rater);
                    personnelUser.setDepartmentname(rater.getDepartment());
                    personnelUser.setUsername(rater.getScorringname());
                    personnelUser.setMoneycard(rater.getScorringcode());
                    personnelUserService.updateByPrimaryKeySelective(personnelUser);
                    map.put("msg", "修改成功");
                    map.put("code", 0);
                } else {
                    map.put("msg", "该部门已存在部门长,如有疑问请联系管理员");
                    map.put("code", 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "修改失败");
            map.put("code", 1);
        }
        return map;
    }


    /**
     * 删除人事部评分人员
     *
     * @param ratercode
     * @return
     */
    @RequestMapping("/delete")
    public Object delete(String ratercode) {
        ModelMap map = new ModelMap();
        try {
            Rater rater = raterService.selectByPrimaryKey(ratercode);
            User user = userService.selectUserByMoneyCard(rater.getScorringcode());
            UserRoleKey userRoleKey = new UserRoleKey();//删除该用户角色
            userRoleKey.setRolecode("500");
            userRoleKey.setUsercode(user.getUsercode());
            userRoleService.deleteByPrimaryKey(userRoleKey);
            List<Role> roles = roleService.selectRoleListByUserCode(user.getUsercode());
            if (roles.size() == 0) {
                user.setFlag("1");
                userService.updateByPrimaryKeySelective(user);
            }
            raterService.deleteByPrimaryKey(ratercode);

            map.put("msg", "删除成功");
            map.put("code", 0);

        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "删除失败");
            map.put("code", 1);
        }
        return map;
    }

    /**
     * 获取部门列表
     */
    @RequestMapping("/getDept")
    public Object getDept() {
        ModelMap map = new ModelMap();
        try {
            List<Rater> raters = raterService.selectDepartList();
            map.put("msg", "查询部门列表成功");
            map.put("total", raters.size());
            map.put("data", raters);
            map.put("code", 0);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(LogUtil.getTrace(e));
            map.put("error", e.getMessage());
            map.put("msg", "查询失败");
            map.put("code", 1);

        }
        return map;
    }
}
