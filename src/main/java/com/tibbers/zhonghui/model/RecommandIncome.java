package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/23 22:15
 * @description:
 */
public class RecommandIncome {
    private String incomeserialno;
    private String accountid;
    private String comefrom;
    private String income;
    private String incomedatetime;
    private String description;
    private String alreadydone;
    private String reverse1;
    private String reverse2;

    @Override
    public String toString() {
        return "RecommandIncome{" +
                "incomeserialno='" + incomeserialno + '\'' +
                ", accountid='" + accountid + '\'' +
                ", comefrom='" + comefrom + '\'' +
                ", income='" + income + '\'' +
                ", incomedatetime='" + incomedatetime + '\'' +
                ", description='" + description + '\'' +
                ", alreadydone='" + alreadydone + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getAlreadydone() {
        return alreadydone;
    }

    public void setAlreadydone(String alreadydone) {
        this.alreadydone = alreadydone;
    }

    public String getIncomeserialno() {
        return incomeserialno;
    }

    public void setIncomeserialno(String incomeserialno) {
        this.incomeserialno = incomeserialno;
    }

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getComefrom() {
        return comefrom;
    }

    public void setComefrom(String comefrom) {
        this.comefrom = comefrom;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getIncomedatetime() {
        return incomedatetime;
    }

    public void setIncomedatetime(String incomedatetime) {
        this.incomedatetime = incomedatetime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
