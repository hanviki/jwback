package com.welb.organization_check.entity;

public class MessageLog {
    private String logcode;

    private String receivercode;

    private String messagecontent;

    private String receivetime;

    private String sendercode;

    public String getLogcode() {
        return logcode;
    }

    public void setLogcode(String logcode) {
        this.logcode = logcode == null ? null : logcode.trim();
    }


    public String getMessagecontent() {
        return messagecontent;
    }

    public void setMessagecontent(String messagecontent) {
        this.messagecontent = messagecontent == null ? null : messagecontent.trim();
    }

    public String getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(String receivetime) {
        this.receivetime = receivetime == null ? null : receivetime.trim();
    }

    public String getReceivercode() {
        return receivercode;
    }

    public void setReceivercode(String receivercode) {
        this.receivercode = receivercode;
    }

    public String getSendercode() {
        return sendercode;
    }

    public void setSendercode(String sendercode) {
        this.sendercode = sendercode;
    }
}
