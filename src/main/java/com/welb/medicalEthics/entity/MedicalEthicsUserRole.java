package com.welb.medicalEthics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalEthicsUserRole {

    /**
     * 主键
     */
    private int  id;

    /**
     * 发薪号
     */
    private String userId;

    /**
     * 角色id
     */
    private int roleId;

    /**
     * 角色代号
     */
    private String roleCode;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 部门id
     */
    private Integer partyId;

    /**
     * 部门登记 1-2-3对应 党委 总支 支部
     */
    private Integer partyLevel;

    /**
     * 部门id
     */
    private Integer departmentId;

}
