package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/20 17:58
 * @description:
 */
public class Product {
    private String productid;
    private String productname;
    private String unitprice;
    private String note;
    private String itemid;
    private String imagepath;
    private String color;
    private int valuescore;
    private String productgrade;
    private String reverse1;
    private String reverse2;

    @Override
    public String toString() {
        return "Product{" +
                "productid='" + productid + '\'' +
                ", productname='" + productname + '\'' +
                ", unitprice='" + unitprice + '\'' +
                ", note='" + note + '\'' +
                ", itemid='" + itemid + '\'' +
                ", imagepath='" + imagepath + '\'' +
                ", color='" + color + '\'' +
                ", valuescore=" + valuescore +
                ", productgrade='" + productgrade + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getValuescore() {
        return valuescore;
    }

    public void setValuescore(int valuescore) {
        this.valuescore = valuescore;
    }

    public String getProductgrade() {
        return productgrade;
    }

    public void setProductgrade(String productgrade) {
        this.productgrade = productgrade;
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
