package com.welb.organization_check.entity;

public class ManualSetTime {
    private Integer id;

    private String year;

    private String month;

    private String time;

    private String createtime;

    private String updatetime;

    private String createmoneycard;

    private String updatemoneycard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year == null ? null : year.trim();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month == null ? null : month.trim();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time == null ? null : time.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime == null ? null : updatetime.trim();
    }

    public String getCreatemoneycard() {
        return createmoneycard;
    }

    public void setCreatemoneycard(String createmoneycard) {
        this.createmoneycard = createmoneycard == null ? null : createmoneycard.trim();
    }

    public String getUpdatemoneycard() {
        return updatemoneycard;
    }

    public void setUpdatemoneycard(String updatemoneycard) {
        this.updatemoneycard = updatemoneycard == null ? null : updatemoneycard.trim();
    }
}
