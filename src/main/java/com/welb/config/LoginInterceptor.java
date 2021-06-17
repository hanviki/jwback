package com.welb.config;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author luoyaozu
 * @title: LoginInterceptor
 * @projectName xh-360appraisal-interface
 * @description: 用户登录拦截器
 * @date 2019/6/3 14:50
 */


@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String usercode = (String)request.getSession().getAttribute("usercode");
        if (usercode==null||usercode.equals("")) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("code", 810);
            jsonObject.addProperty("message", "用户未登录或登录已过期");
            writer.write(jsonObject.toString());
            return false;
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
