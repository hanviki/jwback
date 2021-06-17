package com.welb.sysBase.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 科室用户关联表
 * @author: luox
 * @date： 2020/12/7
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDepartmentUser {

    private Integer id;

    private Integer userId;

    private Integer departmentId;

}
