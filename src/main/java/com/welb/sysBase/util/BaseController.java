package com.welb.sysBase.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.welb.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 类名：UtilController 功能：Controller 父类 详细：所有Controller类的父类 一些常用方法
 * 作者：@author LCL 版本：1.0
 * 日期：2018-6-12
 */
public class BaseController {
    @Autowired
    public HttpServletRequest request;

    /**
     * 将信息格式化成json输出
     *
     * @param message
     * @return
     */
    protected Object ajaxJson(Object message) {
        return JSON.toJSONString(message, SerializerFeature.WriteMapNullValue);
    }

    /**
     * 将信息格式化成JSON输出
     * 禁止循环引用检测
     * @param message
     * @return
     */
    protected Object ajaxJsonDisableCircularReferenceDetect(Object message) {
        return JSON.toJSONString(message, SerializerFeature.DisableCircularReferenceDetect);
    }

    public HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }
    public PageData getPageData() {
        return new PageData(this.getRequest());
    }

}
