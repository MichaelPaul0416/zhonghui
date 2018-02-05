package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/21 23:10
 * @description:
 */
public class AuditingPros {
    private String serialid;
    private String productid;
    private String auditor;
    private String auditstate;
    private String applydatetime;
    private String begindatetime;
    private String enddatetime;

    @Override
    public String toString() {
        return "AuditingPros{" +
                "serialid='" + serialid + '\'' +
                ", productid='" + productid + '\'' +
                ", auditor='" + auditor + '\'' +
                ", auditstate='" + auditstate + '\'' +
                ", applydatetime='" + applydatetime + '\'' +
                ", begindatetime='" + begindatetime + '\'' +
                ", enddatetime='" + enddatetime + '\'' +
                '}';
    }

    public String getSerialid() {
        return serialid;
    }

    public void setSerialid(String serialid) {
        this.serialid = serialid;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getAuditstate() {
        return auditstate;
    }

    public void setAuditstate(String auditstate) {
        this.auditstate = auditstate;
    }

    public String getApplydatetime() {
        return applydatetime;
    }

    public void setApplydatetime(String applydatetime) {
        this.applydatetime = applydatetime;
    }

    public String getBegindatetime() {
        return begindatetime;
    }

    public void setBegindatetime(String begindatetime) {
        this.begindatetime = begindatetime;
    }

    public String getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(String enddatetime) {
        this.enddatetime = enddatetime;
    }
}
