package com.welb.organization_check.entity;

import java.util.ArrayList;
import java.util.List;

public class Branch {
    private String branchcode;

    private String branchname;

    private String upbranchcode;

    private String branchdesc;

    private String fullbranchcode;

    private List<Branch> childBranch =new ArrayList();

    public String getBranchcode() {
        return branchcode;
    }

    public void setBranchcode(String branchcode) {
        this.branchcode = branchcode == null ? null : branchcode.trim();
    }

    public String getBranchname() {
        return branchname;
    }

    public void setBranchname(String branchname) {
        this.branchname = branchname == null ? null : branchname.trim();
    }

    public String getUpbranchcode() {
        return upbranchcode;
    }

    public void setUpbranchcode(String upbranchcode) {
        this.upbranchcode = upbranchcode == null ? null : upbranchcode.trim();
    }

    public String getBranchdesc() {
        return branchdesc;
    }

    public void setBranchdesc(String branchdesc) {
        this.branchdesc = branchdesc == null ? null : branchdesc.trim();
    }

    public List<Branch> getChildBranch() {
        return childBranch;
    }

    public void setChildBranch(List<Branch> childBranch) {
        this.childBranch = childBranch;
    }

    public String getFullbranchcode() {
        return fullbranchcode;
    }

    public void setFullbranchcode(String fullbranchcode) {
        this.fullbranchcode = fullbranchcode== null ? null : fullbranchcode.trim();
    }
}
