package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserSummaryDto;
import com.welb.organization_check.mapper.UserSummaryDtoMapper;
import com.welb.organization_check.service.IUserSummaryDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: UserDtoServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: 首页待评分人业务层接口的实现类
 * @date 2019/6/416:46
 */
@Service
@Transactional
public class UserSummaryDtoServiceImpl implements IUserSummaryDtoService {
    @Resource
    UserSummaryDtoMapper summaryDtoMapper;
    @Override
    public List<UserSummaryDto> selectUserSummary(UserSummaryDto dto) {
        return summaryDtoMapper.selectUserSummary(dto);
    }
    @Override
    public List<UserSummaryDto> selectUserSummaryBySixState(UserSummaryDto dto) {
        return summaryDtoMapper.selectUserSummaryBySixState(dto);
    }

    @Override
    public UserSummaryDto selectUserSummaryByLike(UserSummaryDto dto) {
        return summaryDtoMapper.selectUserSummaryByLike(dto);
    }
}
