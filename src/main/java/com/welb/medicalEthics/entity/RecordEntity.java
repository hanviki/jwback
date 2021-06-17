package com.welb.medicalEthics.entity;

public class RecordEntity {
    private String userId;
    private String name;
    private int row;

    public RecordEntity() {
    }

    public RecordEntity(String userId, String name, int row) {
        this.userId = userId;
        this.name = name;
        this.row = row;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
