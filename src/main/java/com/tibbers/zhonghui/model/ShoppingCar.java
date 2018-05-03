package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/21 16:16
 * @description:
 */
public class ShoppingCar {
    private String serialid;
    private String accountid;
    private String productid;
    private int number;
    private String adddatetime;
    private String modifydatetime;
    private String deleteflag;
    private String reverse1;
    private String reverse2;

    public ShoppingCar(){
        serialid = "";
        accountid = "";
        productid = "";
        number = 1;
        adddatetime = "";
        modifydatetime = "";
        deleteflag = "";
        reverse1 = "";
        reverse2 = " ";
    }

    @Override
    public String toString() {
        return "ShoppingCar{" +
                "serialid='" + serialid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", productid='" + productid + '\'' +
                ", number=" + number +
                ", adddatetime='" + adddatetime + '\'' +
                ", modifydatetime='" + modifydatetime + '\'' +
                ", deleteflag='" + deleteflag + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getModifydatetime() {
        return modifydatetime;
    }

    public void setModifydatetime(String modifydatetime) {
        this.modifydatetime = modifydatetime;
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

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAdddatetime() {
        return adddatetime;
    }

    public void setAdddatetime(String adddatetime) {
        this.adddatetime = adddatetime;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
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
