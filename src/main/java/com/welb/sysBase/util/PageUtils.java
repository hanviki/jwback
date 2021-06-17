package com.welb.sysBase.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageUtils {

    public static List PaginationSetting(Map<String, String> param, List dataSource){
        List returnList = new ArrayList<>();
        int pageSize = Integer.parseInt(param.get("length"));
        int pageNum = Integer.parseInt(param.get("draw"));
        if(!dataSource.isEmpty()){
            for(int i = (pageNum - 1) * pageSize; i < dataSource.size() && i < (pageSize * pageNum); i++){
                returnList.add(dataSource.get(i));
            }
        }

        return returnList;
    }

    public static List PaginationSettingForPages(Map<String, String> param, List dataSource){
        List returnList = new ArrayList<>();
        int pageSize = Integer.parseInt(param.get("pageSize"));
        int pageNum = Integer.parseInt(param.get("pageNum"));
        if(!dataSource.isEmpty()){
            for(int i = (pageNum - 1) * pageSize; i < dataSource.size() && i < (pageSize * pageNum); i++){
                returnList.add(dataSource.get(i));
            }
        }

        return returnList;
    }
}
