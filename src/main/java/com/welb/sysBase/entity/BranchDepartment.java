package com.welb.sysBase.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 支部-科室对应表
 * @author: luox
 * @date： 2020/12/3
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BranchDepartment {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 部门id
     */
    private Integer departmentId;

    /**
     * 支部id
     */
    private Integer branchId;



}
