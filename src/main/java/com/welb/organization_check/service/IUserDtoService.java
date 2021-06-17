package com.welb.organization_check.service;

import com.welb.organization_check.dto.UserDto;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserDtoService
 * @projectName xh-360appraisal-interface
 * @description: 用户dto的业务逻辑层接口
 * @date 2019/6/416:43
 */
public interface IUserDtoService {
    /**
     * 评分汇总和历史评分汇总数据
     *
     * @param userDto
     * @return
     */
    List<UserDto> selectUserDtoLike(UserDto userDto);

    /**
     * 获取打分用户的打分状态历史数据
     *
     * @param userDto
     * @return
     */
    List<UserDto>gradeList(UserDto userDto);
    /**
     * 季度总结管理所有数据
     *
     * @param userDto
     * @return
     */
    List<UserDto> selectUserDtoLikeToQuarter(UserDto userDto);

    /**
     * 获取总数
     * @return
     */
    int getTotalCount(UserDto dto);


    UserDto selectOneUserDto(UserDto dto);
}
