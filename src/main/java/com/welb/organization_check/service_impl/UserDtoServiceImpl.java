package com.welb.organization_check.service_impl;

import com.welb.organization_check.dto.UserDto;
import com.welb.organization_check.mapper.UserDtoMapper;
import com.welb.organization_check.service.IUserDtoService;
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
public class UserDtoServiceImpl implements IUserDtoService {
    @Resource
    UserDtoMapper dtoMapper;
    @Override
    public List<UserDto> selectUserDtoLike(UserDto userDto) {
        return dtoMapper.selectUserDtoLike(userDto);
    }

    @Override
    public List<UserDto> gradeList(UserDto userDto) {
        return dtoMapper.gradeList(userDto);
    }

    @Override
    public List<UserDto> selectUserDtoLikeToQuarter(UserDto userDto) {
        return dtoMapper.selectUserDtoLikeToQuarter(userDto);
    }

    @Override
    public int getTotalCount(UserDto dto) {
        return dtoMapper.getTotalCount(dto);
    }
    @Override
    public UserDto selectOneUserDto(UserDto dto) {
        return dtoMapper.selectOneUserDto(dto);
    }
}
