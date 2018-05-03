package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/23 21:30
 * @description:
 */
public class RefundCerts {
    private String serialid;
    private String refundserialid;
    private String certphotoid;
    private String certphotopath;
    private String uploaddatetime;
    private String deleteflag;
    private String reverse1;
    private String reverse2;


    public RefundCerts(){
        serialid = "";
        refundserialid = "";
        certphotoid = "";
        certphotopath = "";
        uploaddatetime = "";
        deleteflag = "";
        reverse1 = "";
        reverse2 = "";
    }
    @Override
    public String toString() {
        return "RefundCerts{" +
                "serialid='" + serialid + '\'' +
                ", refundserialid='" + refundserialid + '\'' +
                ", certphotoid='" + certphotoid + '\'' +
                ", certphotopath='" + certphotopath + '\'' +
                ", uploaddatetime='" + uploaddatetime + '\'' +
                ", deleteflag='" + deleteflag + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getSerialid() {
        return serialid;
    }

    public void setSerialid(String serialid) {
        this.serialid = serialid;
    }

    public String getRefundserialid() {
        return refundserialid;
    }

    public void setRefundserialid(String refundserialid) {
        this.refundserialid = refundserialid;
    }

    public String getCertphotoid() {
        return certphotoid;
    }

    public void setCertphotoid(String certphotoid) {
        this.certphotoid = certphotoid;
    }

    public String getCertphotopath() {
        return certphotopath;
    }

    public void setCertphotopath(String certphotopath) {
        this.certphotopath = certphotopath;
    }

    public String getUploaddatetime() {
        return uploaddatetime;
    }

    public void setUploaddatetime(String uploaddatetime) {
        this.uploaddatetime = uploaddatetime;
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
