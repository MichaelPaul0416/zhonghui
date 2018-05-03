package com.tibbers.zhonghui.model;

/**
 * @author: Paul
 * @time:2018/1/21 12:58
 * @description:
 */
public class Person {
    private String personid;
    private String personname;
    private String sex;
    private int age;
    private String phone;
    private String address;
    private String bindQQ;
    private String reverse1;
    private String reverse2;
    private String reverse3;

    public Person(){
        personid = "";
        personname = "";
        sex = "";
        age = 1;
        phone = "";
        address = "";
        bindQQ = "";
        reverse1 = "";
        reverse2 = "";
        reverse3 = "";
    }

    @Override
    public String toString() {
        return "Person{" +
                "personid='" + personid + '\'' +
                ", personname='" + personname + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", bindQQ='" + bindQQ + '\'' +
                ", reverse1='" + reverse1 + '\'' +
                ", reverse2='" + reverse2 + '\'' +
                ", reverse3='" + reverse3 + '\'' +
                '}';
    }

    public String getPersonid() {
        return personid;
    }

    public void setPersonid(String personid) {
        this.personid = personid;
    }

    public String getPersonname() {
        return personname;
    }

    public void setPersonname(String personname) {
        this.personname = personname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBindQQ() {
        return bindQQ;
    }

    public void setBindQQ(String bindQQ) {
        this.bindQQ = bindQQ;
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

    public String getReverse3() {
        return reverse3;
    }

    public void setReverse3(String reverse3) {
        this.reverse3 = reverse3;
    }
}
