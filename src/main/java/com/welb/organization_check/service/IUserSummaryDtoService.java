package com.welb.organization_check.service;

import com.welb.organization_check.dto.UserSummaryDto;

import java.util.List;

/**
 * @author luoyaozu
 * @title: IUserDtoService
 * @projectName xh-360appraisal-interface
 * @description: 首页待评分人dto的业务逻辑层接口
 * @date 2019/6/416:43
 */
public interface IUserSummaryDtoService {
    /**
     * 查询待评分人数据和评分完成数据
     * @param dto
     * @return
     */
    List<UserSummaryDto>selectUserSummary(UserSummaryDto dto);
    /**
     * 查询待评分人数据
     * @param dto
     * @return
     */
    List<UserSummaryDto>selectUserSummaryBySixState(UserSummaryDto dto);

    /**
     * 通过评分人的code、年份、季度查询相关信息
     * @param dto
     * @return
     */
    UserSummaryDto selectUserSummaryByLike(UserSummaryDto dto);



}
