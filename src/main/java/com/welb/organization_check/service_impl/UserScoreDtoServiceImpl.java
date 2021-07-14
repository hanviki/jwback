package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.dto.UserScoreDto;
import com.welb.organization_check.entity.User;
import com.welb.organization_check.mapper.UserDtoMapper;
import com.welb.organization_check.mapper.UserScoreDtoMapper;
import com.welb.organization_check.service.IUserDtoService;
import com.welb.organization_check.service.IUserScoreDtoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author luoyaozu
 * @title: UserDtoServiceImpl
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/416:46
 */
@Service
@Transactional
public class UserScoreDtoServiceImpl implements IUserScoreDtoService {
    @Resource
    UserScoreDtoMapper userScoreDtoMapper;

    @Override
    public List<UserScoreDto> findUserScore(String year, String month) {
        return userScoreDtoMapper.findUserScore(year,month);
    }
}
