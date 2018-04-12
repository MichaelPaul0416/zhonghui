package com.tibbers.zhonghui.model;

/**
 * @Author: wangqiang20995
 * @Date:2018/4/12
 * @Description:
 * @Resource:
 */
public class OrderTransport {
    private String serialid;
    private String accountid;
    private String orderid;
    private String familyid;
    private String costamount;
    private String limitcustomfee;
    private String orderdatetime;
    private String transportfee;
    private String isvalid;
    private String reverse1;
    private String reverse2;

    public OrderTransport(){
        reverse2 = "";
    }
    @Override
    public String toString() {
        return "OrderTransport{" +
                "serialid='" + serialid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", familyid='" + familyid + '\'' +
                ", costamount='" + costamount + '\'' +
                ", limitcustomfee='" + limitcustomfee + '\'' +
                ", orderdatetime='" + orderdatetime + '\'' +
                ", transportfee='" + transportfee + '\'' +
                ", isvalid='" + isvalid + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }

    public String getSerialid() {
        return serialid;
    }

    public void setSerialid(String serialid) {
        this.serialid = serialid;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getCostamount() {
        return costamount;
    }

    public void setCostamount(String costamount) {
        this.costamount = costamount;
    }

    public String getLimitcustomfee() {
        return limitcustomfee;
    }

    public void setLimitcustomfee(String limitcustomfee) {
        this.limitcustomfee = limitcustomfee;
    }

    public String getOrderdatetime() {
        return orderdatetime;
    }

    public void setOrderdatetime(String orderdatetime) {
        this.orderdatetime = orderdatetime;
    }

    public String getTransportfee() {
        return transportfee;
    }

    public void setTransportfee(String transportfee) {
        this.transportfee = transportfee;
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
