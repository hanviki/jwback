package com.welb.util;

 import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.*;

/**
 * 说明：参数封装Map 创建人：LCL 修改时间：2018-6-12
 */

public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 1L;

    Map map = null;
    HttpServletRequest request;

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        if (returnMap.get("u_id") == null) {
            returnMap.put("u_id", request.getHeader("u_id"));
        }
        if (returnMap.get("data") != null) {
            returnMap.putAll((Map) JSON.parse((String) returnMap.get("data")));
        }
        map = returnMap;
    }

    public PageData() {
        map = new HashMap();
    }

    @Override
    public Object get(Object key) {
        Object obj = null;
        if (map.get(key) instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = map.get(key);
        }
        return obj;
    }

    public String getString(Object key) {
        if (get(key) == null) {
            return "";
        }
        return get(key).toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        if (value instanceof ClobProxyImpl) { // 读取oracle Clob类型数据
            try {
                ClobProxyImpl cpi = (ClobProxyImpl) value;
                Reader is = cpi.getCharacterStream(); // 获取流
                BufferedReader br = new BufferedReader(is);
                String str = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (str != null) { // 循环读取数据拼接到字符串
                    sb.append(str);
                    sb.append("\n");
                    str = br.readLine();
                }
                value = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }
    @Override
    public void clear() {
        map.clear();
    }
    @Override
    public boolean containsKey(Object key) {
        // TODO Auto-generated method stub
        return map.containsKey(key);
    }
    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return map.containsValue(value);
    }
    @Override
    public Set entrySet() {
        // TODO Auto-generated method stub
        return map.entrySet();
    }
    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return map.isEmpty();
    }
    @Override
    public Set keySet() {
        // TODO Auto-generated method stub
        return map.keySet();
    }
    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map t) {
        // TODO Auto-generated method stub
        map.putAll(t);
    }
    @Override
    public int size() {
        // TODO Auto-generated method stub
        return map.size();
    }
    @Override
    public Collection values() {
        // TODO Auto-generated method stub
        return map.values();
    }

}
