package com.tibbers.zhonghui.model.common;

/**
 * @author: Paul
 * @time:2018/1/23 23:21
 * @description:
 */
public class Compares {
    private String compareBegin;
    private String compareEnd;
    private String compareSepator;

    @Override
    public String toString() {
        return "Compares{" +
                "compareBegin='" + compareBegin + '\'' +
                ", compareEnd='" + compareEnd + '\'' +
                ", compareSepator='" + compareSepator + '\'' +
                '}';
    }

    public Compares(String compareBegin, String compareEnd, String compareSepator) {
        this.compareBegin = compareBegin;
        this.compareEnd = compareEnd;
        this.compareSepator = compareSepator;
    }

    public String getCompareBegin() {

        return compareBegin;
    }

    public void setCompareBegin(String compareBegin) {
        this.compareBegin = compareBegin;
    }

    public String getCompareEnd() {
        return compareEnd;
    }

    public void setCompareEnd(String compareEnd) {
        this.compareEnd = compareEnd;
    }

    public String getCompareSepator() {
        return compareSepator;
    }

    public void setCompareSepator(String compareSepator) {
        this.compareSepator = compareSepator;
    }
}
