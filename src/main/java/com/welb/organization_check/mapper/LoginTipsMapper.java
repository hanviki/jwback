package com.welb.organization_check.mapper;

import com.welb.organization_check.entity.LoginTips;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author luoyaozu
 * @title: LoginTipsMapper
 * @projectName xh-360appraisal-interface
 * @description: TODO
 * @date 2019/6/1811:43
 */
@Mapper
public interface LoginTipsMapper {

    int updateLoginTips(LoginTips loginTips);

    LoginTips selectLoginTips(String rolecode);
}
