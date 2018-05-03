package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/23 0:33
 * @description:
 */
public class Recommand {
    private String serialid;
    private String accountid;
    private String recommander;
    private String recommanddate;
    private String recommandtime;
    private String reverse1;
    private String reverse2;

    public Recommand(){
        serialid = "";
        accountid = "";
        recommanddate = "";
        recommanddate = "";
        recommander = "";
        reverse1 = "";
        reverse2 = "";
    }
    @Override
    public String toString() {
        return "Recommand{" +
                "serialid='" + serialid + '\'' +
                ", accountid='" + accountid + '\'' +
                ", recommander='" + recommander + '\'' +
                ", recommanddate='" + recommanddate + '\'' +
                ", recommandtime='" + recommandtime + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
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

    public String getRecommander() {
        return recommander;
    }

    public void setRecommander(String recommander) {
        this.recommander = recommander;
    }

    public String getRecommanddate() {
        return recommanddate;
    }

    public void setRecommanddate(String recommanddate) {
        this.recommanddate = recommanddate;
    }

    public String getRecommandtime() {
        return recommandtime;
    }

    public void setRecommandtime(String recommandtime) {
        this.recommandtime = recommandtime;
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
