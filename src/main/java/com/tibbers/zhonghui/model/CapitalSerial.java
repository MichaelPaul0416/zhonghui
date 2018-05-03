package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/23 21:53
 * @description:
 */
public class CapitalSerial {
    private String emcapitalserial;
    private String thirdpartserial;
    private String orderid;
    private String capitaldatetime;
    private String state;
    private String thirdpartmsg;
    private String capitaldirect;
    private String reverse1;
    private String reverse2;

    public CapitalSerial(){
        emcapitalserial = "";
        thirdpartserial = "";
        orderid = "";
        capitaldatetime = "";
        state = "";
        thirdpartmsg = "";
        capitaldirect = "";
        reverse1 = "";
        reverse2 = "";
    }
    @Override
    public String toString() {
        return "CapitalSerial{" +
                "emcapitalserial='" + emcapitalserial + '\'' +
                ", thirdpartserial='" + thirdpartserial + '\'' +
                ", orderid='" + orderid + '\'' +
                ", capitaldatetime='" + capitaldatetime + '\'' +
                ", state='" + state + '\'' +
                ", thirdpartmsg='" + thirdpartmsg + '\'' +
                ", capitaldirect='" + capitaldirect + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                '}';
    }

    public String getEmcapitalserial() {
        return emcapitalserial;
    }

    public void setEmcapitalserial(String emcapitalserial) {
        this.emcapitalserial = emcapitalserial;
    }

    public String getThirdpartserial() {
        return thirdpartserial;
    }

    public void setThirdpartserial(String thirdpartserial) {
        this.thirdpartserial = thirdpartserial;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCapitaldatetime() {
        return capitaldatetime;
    }

    public void setCapitaldatetime(String capitaldatetime) {
        this.capitaldatetime = capitaldatetime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getThirdpartmsg() {
        return thirdpartmsg;
    }

    public void setThirdpartmsg(String thirdpartmsg) {
        this.thirdpartmsg = thirdpartmsg;
    }

    public String getCapitaldirect() {
        return capitaldirect;
    }

    public void setCapitaldirect(String capitaldirect) {
        this.capitaldirect = capitaldirect;
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
