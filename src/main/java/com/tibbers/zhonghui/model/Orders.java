package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/23 21:55
 * @description:
 */
public class Orders {

    private String addressid;
    private String accountid;
    private String orderid;
    private String createdatetime;
    private String orderstate;
    private String isvalid;
    private String expressno;
    private String donedatetime;
    private String freetransport;
    private String transportfee;
    private String note;
    private String amount;
    private String paybybalance;
    private String reverse1;
    private String reverse2;

    @Override
    public String toString() {
        return "Orders{" +
                "addressid='" + addressid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", createdatetime='" + createdatetime + '\'' +
                ", orderstate='" + orderstate + '\'' +
                ", isvalid='" + isvalid + '\'' +
                ", expressno='" + expressno + '\'' +
                ", donedatetime='" + donedatetime + '\'' +
                ", freetransport='" + freetransport + '\'' +
                ", transportfee='" + transportfee + '\'' +
                ", note='" + note + '\'' +
                ", amount='" + amount + '\'' +
                ", paybybalance='" + paybybalance + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getPaybybalance() {
        return paybybalance;
    }

    public void setPaybybalance(String paybybalance) {
        this.paybybalance = paybybalance;
    }


    public String getAddressid() {
        return addressid;
    }

    public void setAddressid(String addressid) {
        this.addressid = addressid;
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

    public String getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(String createdatetime) {
        this.createdatetime = createdatetime;
    }

    public String getOrderstate() {
        return orderstate;
    }

    public void setOrderstate(String orderstate) {
        this.orderstate = orderstate;
    }

    public String getIsvalid() {
        return isvalid;
    }

    public void setIsvalid(String isvalid) {
        this.isvalid = isvalid;
    }

    public String getExpressno() {
        return expressno;
    }

    public void setExpressno(String expressno) {
        this.expressno = expressno;
    }

    public String getDonedatetime() {
        return donedatetime;
    }

    public void setDonedatetime(String donedatetime) {
        this.donedatetime = donedatetime;
    }

    public String getFreetransport() {
        return freetransport;
    }

    public void setFreetransport(String freetransport) {
        this.freetransport = freetransport;
    }

    public String getTransportfee() {
        return transportfee;
    }

    public void setTransportfee(String transportfee) {
        this.transportfee = transportfee;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
