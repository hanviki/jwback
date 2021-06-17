package com.welb.personnel_check.entity;

public class PersonnelAuthorization {
    private Integer id;

    private String agent;

    private String deptuser;

    private String starttime;

    private String endtime;

    private String flag;

    private String departmentname;

    private String agentusername;

    private String deptusername;

    private String ispersonnel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent == null ? null : agent.trim();
    }

    public String getDeptuser() {
        return deptuser;
    }

    public void setDeptuser(String deptuser) {
        this.deptuser = deptuser == null ? null : deptuser.trim();
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime == null ? null : starttime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag == null ? null : flag.trim();
    }


    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getAgentusername() {
        return agentusername;
    }

    public void setAgentusername(String agentusername) {
        this.agentusername = agentusername;
    }

    public String getDeptusername() {
        return deptusername;
    }

    public void setDeptusername(String deptusername) {
        this.deptusername = deptusername;
    }

    public String getIspersonnel() {
        return ispersonnel;
    }

    public void setIspersonnel(String ispersonnel) {
        this.ispersonnel = ispersonnel;
    }
}
