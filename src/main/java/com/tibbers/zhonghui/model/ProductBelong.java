package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/21 0:04
 * @description:
 */
public class ProductBelong {
    private String serialid;

    private String productid;

    private String accountid;

    private String salestate;

    private int remaindernum;

    private String familyid;

    public ProductBelong(){
        serialid = "";
        productid = "";
        accountid = "";
        salestate = "";
        remaindernum = 0;
        familyid = "";
    }
    @Override
    public String toString() {
        return "ProductBelong{" +
                "serialid='" + serialid + '\'' +
                ", productid='" + productid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", salestate='" + salestate + '\'' +
                ", remaindernum=" + remaindernum +
                ", familyid='" + familyid + '\'' +
                '}';
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
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

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getSalestate() {
        return salestate;
    }

    public void setSalestate(String salestate) {
        this.salestate = salestate;
    }

    public int getRemaindernum() {
        return remaindernum;
    }

    public void setRemaindernum(int remaindernum) {
        this.remaindernum = remaindernum;
    }
}
