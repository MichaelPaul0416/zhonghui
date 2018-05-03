package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/22 21:43
 * @description:
 */
public class ReceiveAddress {
    private String serialid;
    private String accountid;
    private String receivename;
    private String contactphone;
    private String postcode;
    private String province;
    private String city;
    private String area;
    private String street;
    private String addressmore;
    private String totaladdress;
    private String deleteflag;
    private String defaultaddress;
    private String reverse;

    public ReceiveAddress(){
        serialid = "";
        accountid = "";
        receivename = "";
        contactphone = "";
        postcode = "";
        province = "";
        city = "";
        area = "";
        street = "";
        addressmore = "";
        totaladdress = "";
        defaultaddress = "";
        defaultaddress = "";
        reverse = "";
    }
    @Override
    public String toString() {
        return "ReceiveAddress{" +
                "serialid='" + serialid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", receivename='" + receivename + '\'' +
                ", contactphone='" + contactphone + '\'' +
                ", postcode='" + postcode + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", street='" + street + '\'' +
                ", addressmore='" + addressmore + '\'' +
                ", totaladdress='" + totaladdress + '\'' +
                ", deleteflag='" + deleteflag + '\'' +
                ", defaultaddress='" + defaultaddress + '\'' +
                ", reverse='" + reverse + '\'' +
                '}';
    }

    public String getDefaultaddress() {
        return defaultaddress;
    }

    public void setDefaultaddress(String defaultaddress) {
        this.defaultaddress = defaultaddress;
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

    public String getReceivename() {
        return receivename;
    }

    public void setReceivename(String receivename) {
        this.receivename = receivename;
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getAddressmore() {
        return addressmore;
    }

    public void setAddressmore(String addressmore) {
        this.addressmore = addressmore;
    }

    public String getTotaladdress() {
        return totaladdress;
    }

    public void setTotaladdress(String totaladdress) {
        this.totaladdress = totaladdress;
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }
}
