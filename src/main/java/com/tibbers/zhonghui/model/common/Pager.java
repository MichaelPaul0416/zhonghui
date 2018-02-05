package com.tibbers.zhonghui.model.common;

/**
 * @author: Paul
 * @time:2018/1/21 0:23
 * @description:
 */
public class Pager {
    private int startLine;
    private int offset;

    public Pager(int startLine, int offset) {
        this.startLine = startLine;
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "Pager{" +
                "startLine=" + startLine +
                ", offset=" + offset +
                '}';
    }

    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
