package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/23 22:02
 * @description:
 */
public class Refund {
    private String refundserialid;
    private String accountid;
    private String orderid;
    private String productid;
    private int number;
    private String amount;
    private String itemid;
    private String detail;
    private String applydatetime;
    private String agreestate;
    private String dealdatetime;
    private String agreedatetime;
    private String rejectreason;
    private String reverse1;
    private String reverse2;

    @Override
    public String toString() {
        return "Refund{" +
                "refundserialid='" + refundserialid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", productid='" + productid + '\'' +
                ", number=" + number +
                ", amount='" + amount + '\'' +
                ", itemid='" + itemid + '\'' +
                ", detail='" + detail + '\'' +
                ", applydatetime='" + applydatetime + '\'' +
                ", agreestate='" + agreestate + '\'' +
                ", dealdatetime='" + dealdatetime + '\'' +
                ", agreedatetime='" + agreedatetime + '\'' +
                ", rejectreason='" + rejectreason + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getRejectreason() {
        return rejectreason;
    }

    public void setRejectreason(String rejectreason) {
        this.rejectreason = rejectreason;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getRefundserialid() {
        return refundserialid;
    }

    public void setRefundserialid(String refundserialid) {
        this.refundserialid = refundserialid;
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

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getApplydatetime() {
        return applydatetime;
    }

    public void setApplydatetime(String applydatetime) {
        this.applydatetime = applydatetime;
    }

    public String getAgreestate() {
        return agreestate;
    }

    public void setAgreestate(String agreestate) {
        this.agreestate = agreestate;
    }

    public String getDealdatetime() {
        return dealdatetime;
    }

    public void setDealdatetime(String dealdatetime) {
        this.dealdatetime = dealdatetime;
    }

    public String getAgreedatetime() {
        return agreedatetime;
    }

    public void setAgreedatetime(String agreedatetime) {
        this.agreedatetime = agreedatetime;
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
