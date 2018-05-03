package com.tibbers.zhonghui.model;

/**
 * @Author: wangqiang20995
 * @Date:2018/3/20
 * @Description:
 * @Resource:
 */
public class WithDraw {
    private String serialid;
    private String accountid;
    private String applydatetime;
    private String amount;
    private String applystate;
    private String adminid;
    private String agreedatetime;
    private String refusereason;
    private String reverse1;
    private String reverse2;

    public WithDraw(){
        serialid = "";
        accountid = "";
        applydatetime = "";
        amount = "";
        applystate = "";
        adminid = "";
        agreedatetime = "";
        refusereason = "";
        reverse1 = "";
        reverse2 = "";
    }
    @Override
    public String toString() {
        return "WithDraw{" +
                "serialid='" + serialid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", applydatetime='" + applydatetime + '\'' +
                ", amount='" + amount + '\'' +
                ", applystate='" + applystate + '\'' +
                ", adminid='" + adminid + '\'' +
                ", agreedatetime='" + agreedatetime + '\'' +
                ", refusereason='" + refusereason + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getRefusereason() {
        return refusereason;
    }

    public void setRefusereason(String refusereason) {
        this.refusereason = refusereason;
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

    public String getApplydatetime() {
        return applydatetime;
    }

    public void setApplydatetime(String applydatetime) {
        this.applydatetime = applydatetime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApplystate() {
        return applystate;
    }

    public void setApplystate(String applystate) {
        this.applystate = applystate;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
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
