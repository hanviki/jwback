package com.welb.organization_check.service;

import com.welb.organization_check.entity.AssessmentState;

/**
 * @author luoyaozu
 * @title: IAssessmentStateService
 * @projectName xh-360appraisal-interface
 * @description: 是否开启手动考核按钮接口
 * @date 2020/2/1611:19
 */
public interface IAssessmentStateService {
    AssessmentState selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AssessmentState state);
}
