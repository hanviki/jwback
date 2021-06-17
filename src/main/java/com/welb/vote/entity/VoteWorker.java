package com.welb.vote.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class VoteWorker {
    public static final Map<String,String>typeList=new LinkedHashMap<>();

    static {
        typeList.put("1","医师");
        typeList.put("2","护理");
        typeList.put("3","技师");
    }


    private String workerId;

    private String workerName;

    private String type;

    private String typeName;

    private String picture;

    private String info;

    private int count;

    private String dept;

    private String code;

    private String flag;//1-为投票  2-已投票

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static Map<String, String> getTypeList() {
        return typeList;
    }

    public String getTypeName() {
        if (type!=null){
            return typeList.get(this.type);
        }
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
