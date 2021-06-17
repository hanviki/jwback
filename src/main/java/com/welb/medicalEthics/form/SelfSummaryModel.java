package com.welb.medicalEthics.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 自我总结信息提交
 * @author: luox
 * @date： 2020/11/25
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SelfSummaryModel {

    /**
     * 用户名
     */
    private String userId;

    /**
     * 自我总结
     */
    private String selfSummary;

}
