package com.welb.organization_check.entity;

public class MessageTemplate {
    private String templatecode;

    private String templatename;

    private String templatecontent;

    private String flag;

    public String getTemplatecode() {
        return templatecode;
    }

    public void setTemplatecode(String templatecode) {
        this.templatecode = templatecode == null ? null : templatecode.trim();
    }

    public String getTemplatename() {
        return templatename;
    }

    public void setTemplatename(String templatename) {
        this.templatename = templatename == null ? null : templatename.trim();
    }

    public String getTemplatecontent() {
        return templatecontent;
    }

    public void setTemplatecontent(String templatecontent) {
        this.templatecontent = templatecontent == null ? null : templatecontent.trim();
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
