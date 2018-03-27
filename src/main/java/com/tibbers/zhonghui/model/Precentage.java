package com.tibbers.zhonghui.model;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/26
 * @Description:
 * @Resource:平台提成收益
 */
public class Precentage {
    private String serialid;
    private String orderid;
    private String precentage;
    private String donedatetime;
    private String reverse1;

    public Precentage(){
        reverse1 = "";
    }
    @Override
    public String toString() {
        return "Precentage{" +
                "serialid='" + serialid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", precentage='" + precentage + '\'' +
                ", donedatetime='" + donedatetime + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                '}';
    }

    public String getSerialid() {
        return serialid;
    }

    public void setSerialid(String serialid) {
        this.serialid = serialid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPrecentage() {
        return precentage;
    }

    public void setPrecentage(String precentage) {
        this.precentage = precentage;
    }

    public String getDonedatetime() {
        return donedatetime;
    }

    public void setDonedatetime(String donedatetime) {
        this.donedatetime = donedatetime;
    }

    public String getReverse1() {
        return reverse1;
    }

    public void setReverse1(String reverse1) {
        this.reverse1 = reverse1;
    }
}
