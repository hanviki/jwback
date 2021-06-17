package com.welb.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author luoyaozu
 * @title: LoginConfig
 * @projectName xh-360appraisal-interface
 * @description:
 * @date 2019/6/421:40
 */

@Configuration
public class LoginConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(loginInterceptor).addPathPatterns("360kao/**").excludePathPatterns("360kao/login");
    }
}