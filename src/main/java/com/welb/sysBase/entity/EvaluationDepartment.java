package com.welb.sysBase.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 科室管理
 * @author: luox
 * @date： 2020/12/3
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDepartment {

    /**
     * id
     */
    private Integer id;

    /**
     * 科室名字
     */
    private String departmentName;

    /**
     * 主任id
     */
    private Integer directorUserId;

    /**
     * 主任名字
     */
    private String directorName;

    /**
     * 书记名字
     */
    private String secretaryName;


    /**
     * 书记id
     */
    private Integer secretaryUserId;

    /**
     * 支部名字
     */
    private String branchName;

    /**
     * 支部id
     */
    private Integer branchId;

    /**
     * 总支名字
     */
    private String generalBranchName;

    /**
     * 党委名字
     */
    private String partyCommitteesName;

    /**
     * 部门排序
     */
    private Integer departmentOrder;

    /**
     * 查询的名字
     */
    private String queryName;

    private List<String> partyArray;


}
