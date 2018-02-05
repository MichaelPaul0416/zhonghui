package com.tibbers.zhonghui.model;

import java.io.Serializable;

/**
 * @author: Paul
 * @time:2018/1/20 9:38
 * @description:
 */
public class SysParam implements Serializable{
    private String categoryid;
    private String itemid;
    private String itemname;
    private String caption;
    private String description;
    private String deleteflag;
    private String reverse;

    @Override
    public String toString() {
        return "SysParam{" +
                "categoryid='" + categoryid + '\'' +
                ", itemid='" + itemid + '\'' +
                ", itemname='" + itemname + '\'' +
                ", caption='" + caption + '\'' +
                ", description='" + description + '\'' +
                ", deleteflag='" + deleteflag + '\'' +
                ", reverse='" + reverse + '\'' +
                '}';
    }

    public String getDeleteflag() {
        return deleteflag;
    }

    public void setDeleteflag(String deleteflag) {
        this.deleteflag = deleteflag;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReverse() {
        return reverse;
    }

    public void setReverse(String reverse) {
        this.reverse = reverse;
    }
}
