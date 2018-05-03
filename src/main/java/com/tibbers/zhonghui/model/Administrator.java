package com.tibbers.zhonghui.model;

import java.io.Serializable;

/**
 * @author: Paul
 * @time:2018/2/26 22:11
 * @description:
 */
public class Administrator implements Serializable{
    private String serialid;
    private String adminid;
    private String adminname;
    private String adminpassword;
    private String createdatetime;
    private String lastmodifydatetime;
    private String identitytype;
    private String isvalid;
    private String reverse1;
    private String reverse2;

    public Administrator(){
        serialid = "";
        adminid = "";
        adminname = "";
        adminpassword = "";
        createdatetime = "";
        lastmodifydatetime = "";
        identitytype = "";
        isvalid = "";
        reverse1 = "";
        reverse2 = "";
    }

    @Override
    public String toString() {
        return "Administrator{" +
                "serialid='" + serialid + '\'' +
                ", adminid='" + adminid + '\'' +
                ", adminname='" + adminname + '\'' +
                ", adminpassword='" + adminpassword + '\'' +
                ", createdatetime='" + createdatetime + '\'' +
                ", lastmodifydatetime='" + lastmodifydatetime + '\'' +
                ", identitytype='" + identitytype + '\'' +
                ", isvalid='" + isvalid + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getIdentitytype() {
        return identitytype;
    }

    public void setIdentitytype(String identitytype) {
        this.identitytype = identitytype;
    }

    public String getSerialid() {
        return serialid;
    }

    public void setSerialid(String serialid) {
        this.serialid = serialid;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getAdminpassword() {
        return adminpassword;
    }

    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }

    public String getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(String createdatetime) {
        this.createdatetime = createdatetime;
    }

    public String getLastmodifydatetime() {
        return lastmodifydatetime;
    }

    public void setLastmodifydatetime(String lastmodifydatetime) {
        this.lastmodifydatetime = lastmodifydatetime;
    }

    public String getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }

    public String getReverse1() {
        return reverse1;
    }

    public void setReverse1(String reverse1) {
        this.reverse1 = reverse1;
    }

    public String getReverse2() {
        return reverse2;
    }

    public void setReverse2(String reverse2) {
        this.reverse2 = reverse2;
    }
}
