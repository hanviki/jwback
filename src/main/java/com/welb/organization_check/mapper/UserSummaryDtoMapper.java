package com.welb.organization_check.mapper;

import com.welb.organization_check.dto.UserSummaryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author luoyaozu
 * @title: UserSummaryDtoMapper
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/512:52
 */
@Mapper
public interface UserSummaryDtoMapper {

    List<UserSummaryDto>selectUserSummary(UserSummaryDto dto);

    List<UserSummaryDto>selectUserSummaryBySixState(UserSummaryDto dto);

    UserSummaryDto selectUserSummaryByLike(UserSummaryDto dto);


}
