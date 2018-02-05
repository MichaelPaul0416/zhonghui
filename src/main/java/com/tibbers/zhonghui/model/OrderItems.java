package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/23 21:59
 * @description:
 */
public class OrderItems {
    private String serialid;
    private String orderid;
    private String productid;
    private int pronumber;
    private String shopcarid;
    private int dicount;
    private String note;
    private String reverse1;
    private String reverse2;

    @Override
    public String toString() {
        return "OrderItems{" +
                "serialid='" + serialid + '\'' +
                ", orderid='" + orderid + '\'' +
                ", productid='" + productid + '\'' +
                ", pronumber=" + pronumber +
                ", shopcarid='" + shopcarid + '\'' +
                ", dicount=" + dicount +
                ", note='" + note + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getShopcarid() {
        return shopcarid;
    }

    public void setShopcarid(String shopcarid) {
        this.shopcarid = shopcarid;
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

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public int getPronumber() {
        return pronumber;
    }

    public void setPronumber(int pronumber) {
        this.pronumber = pronumber;
    }

    public int getDicount() {
        return dicount;
    }

    public void setDicount(int dicount) {
        this.dicount = dicount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
