package com.welb.medicalEthics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 医德医风字典
 * @author: luox
 * @date： 2020/12/23
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalEthicsDic {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 字典类型
     */
    private String dicType;

    /**
     * 字典编码
     */
    private String dicCode;

    /**
     * 字典中文值
     */
    private String dicValue;

}
