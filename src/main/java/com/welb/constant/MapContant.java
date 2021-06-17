package com.welb.constant;

import java.util.HashMap;

/**
 * @author luoyaozu
 * @title: MapContant
 * @projectName xh-360appraisal-interface
 * @description: map集合常量
 * @date 2020/7/22
 */
public class MapContant {
    private static final HashMap<String,Object>map=new HashMap<>();

    public static final int count=5;

    /**
     * 往map集合添加元素
     * @param s
     * @param o
     */
    public static void setMap(String s,Object o){
         map.put(s,o);
    }

    /**
     * 获取元素
     * @param s
     * @return
     */
    public static Object getMap(String s){

         return map.get(s);
    }

    /**
     * 删除元素
     * @param s
     */
    public static void deleteMap(String s){

         map.remove(s);
    }

}
