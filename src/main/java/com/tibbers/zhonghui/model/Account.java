package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/21 13:00
 * @description:
 */
public class Account {
    private String accountid;
    private String personid;
    private String accountname;
    private String imagepath;
    private String bindphone;
    private String score;
    private String isvip;
    private String custtype;
    private String accobalance;
    private String reverse;

    public Account(){
        reverse = "";
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountid='" + accountid + '\'' +
                ", personid='" + personid + '\'' +
                ", accountname='" + accountname + '\'' +
                ", imagepath='" + imagepath + '\'' +
                ", bindphone='" + bindphone + '\'' +
                ", score='" + score + '\'' +
                ", isvip='" + isvip + '\'' +
                ", custtype='" + custtype + '\'' +
                ", accobalance='" + accobalance + '\'' +
                ", reverse='" + reverse + '\'' +
                '}';
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }

    public String getAccobalance() {
        return accobalance;
    }

    public void setAccobalance(String accobalance) {
        this.accobalance = accobalance;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getBindphone() {
        return bindphone;
    }

    public void setBindphone(String bindphone) {
        this.bindphone = bindphone;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getIsvip() {
        return isvip;
    }

    public void setIsvip(String isvip) {
        this.isvip = isvip;
    }

    public String getCusttype() {
        return custtype;
    }

    public void setCusttype(String custtype) {
        this.custtype = custtype;
    }

}
