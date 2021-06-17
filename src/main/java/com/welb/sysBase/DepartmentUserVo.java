package com.welb.sysBase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: luox
 * @date： 2020/12/7
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentUserVo {

    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 科室名字
     */
    private String departmentName;

    /**
     * 党支部信息
     */
    private String partyBranchName;

    /**
     * 党支部信息
     */
    private String generalBranchName;
    /**
     * 党委名名字
     */
    private String committeesName;
    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 人员类型
     */
    private String personType;

    /**
     * 当前步骤
     */
    private Integer step;

    /**
     * 打分书记名字
     */
    private String partyDirectorName;

    /**
     * 科室主管名字
     */
    private String departmentDirectorName;



}
