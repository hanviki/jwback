package com.welb.medicalEthics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: luox
 * @date： 2021/2/1
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalPermDto {

    /**
     * 发薪号
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 状态 0-启用  1-禁用
     */
    private String status;

    /**
     * 权限名
     */
    private String roleName;

    private String roleId;

}
