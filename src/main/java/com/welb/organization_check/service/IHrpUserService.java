package com.welb.organization_check.service;

import com.welb.organization_check.entity.HrpUser;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IHrpUsersERVICE
 * @projectName xh-360appraisal-interface
 * @description: hrp用户数据
 * @date 2019/8/2910:50
 */
public interface IHrpUserService {
    /**
     * 通过主键(发薪号)查找用户信息
     * @param uId
     * @return
     */

    HrpUser selectByPrimaryKey(String uId);

    List<HrpUser> selectAll();

}
