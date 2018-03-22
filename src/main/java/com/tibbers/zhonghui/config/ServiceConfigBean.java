package com.tibbers.zhonghui.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: Paul
 * @time:2018/1/21 1:03
 * @description:
 */
@Component
public class ServiceConfigBean {

    @Value("#{prop.absoluteProductPathPrefix}")
    private String absoluteProductPathPrefix;

    @Value("#{prop.absoluteAccountPathPrefix}")
    private String absoluteAccountPathPrefix;

    @Value("#{prop.defaultImagePath}")
    private String defaultImagePath;

    @Value("#{prop.refundcertsPathPrefix}")
    private String refundcertsPathPrefix;

    @Value("#{prop.refundScanSwitch}")
    private String refundScanSwitch;

    @Value("#{prop.wxenvcodeImagePathPrefix}")
    private String wxenvcodeImagePathPrefix;

    public String getWxenvcodeImagePathPrefix() {
        return wxenvcodeImagePathPrefix;
    }

    public void setWxenvcodeImagePathPrefix(String wxenvcodeImagePathPrefix) {
        this.wxenvcodeImagePathPrefix = wxenvcodeImagePathPrefix;
    }

    public String getRefundScanSwitch() {
        return refundScanSwitch;
    }

    public void setRefundScanSwitch(String refundScanSwitch) {
        this.refundScanSwitch = refundScanSwitch;
    }

    public String getRefundcertsPathPrefix() {
        return refundcertsPathPrefix;
    }

    public void setRefundcertsPathPrefix(String refundcertsPathPrefix) {
        this.refundcertsPathPrefix = refundcertsPathPrefix;
    }

    public String getDefaultImagePath() {
        return defaultImagePath;
    }

    public void setDefaultImagePath(String defaultImagePath) {
        this.defaultImagePath = defaultImagePath;
    }

    public String getAbsoluteAccountPathPrefix() {
        return absoluteAccountPathPrefix;
    }

    public void setAbsoluteAccountPathPrefix(String absoluteAccountPathPrefix) {
        this.absoluteAccountPathPrefix = absoluteAccountPathPrefix;
    }

    public String getAbsoluteProductPathPrefix() {
        return absoluteProductPathPrefix;
    }

    public void setAbsoluteProductPathPrefix(String absoluteProductPathPrefix) {
        this.absoluteProductPathPrefix = absoluteProductPathPrefix;
    }
}
