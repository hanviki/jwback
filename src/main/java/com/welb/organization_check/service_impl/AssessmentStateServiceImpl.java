package com.welb.organization_check.service_impl;

import com.welb.organization_check.entity.AssessmentState;
import com.welb.organization_check.mapper.AssessmentStateMapper;
import com.welb.organization_check.service.IAssessmentStateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: AssessmentStateServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 是否开启手动考核按钮接口实现类
 * @date 2020/2/1611:22
 */
@Service
@Transactional
public class AssessmentStateServiceImpl  implements IAssessmentStateService {
    @Resource
    AssessmentStateMapper stateMapper;
    @Override
    public AssessmentState selectByPrimaryKey(Integer id) {
        return stateMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(AssessmentState state) {
        return stateMapper.updateByPrimaryKeySelective(state);
    }
}

