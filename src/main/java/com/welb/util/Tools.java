package com.welb.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：常用工具
 * 创建人：LCL
 * 修改时间：2018-6-13
 * @version
 */
public class Tools {

	/**
	 * 得到request对象
	 *
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	/**
	 * 获取参数封装map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParamMap(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		Enumeration enu = request.getParameterNames();
		while(enu.hasMoreElements()){
			String name = (String)enu.nextElement();
			if(!Tools.isEmpty(request.getParameter(name))){
				map.put(name, request.getParameter(name));
			}
		}
		map.put("u_id", request.getHeader("u_id"));
		return map;
	}
	/**
	 * 检测字符串是否为空(null,"","null")
	 * @param s
	 * @return 为空则返回true，不否则返回false
	 */
	public static boolean isEmpty(String s){
		return s==null || "".equals(s) || "null".equals(s);
	}

}
