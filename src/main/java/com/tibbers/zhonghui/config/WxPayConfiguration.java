package com.tibbers.zhonghui.config;

import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 微信支付相关配置
 * <p>
 * Created by bjliumingbo on 2017/5/12.
 */
@Configuration
@Component
public class WxPayConfiguration {
	@Value("#{wxPayProperties.appId}")
	private String appId;

	@Value("#{wxPayProperties.mchId}")
	private String mchId;

	@Value("#{wxPayProperties.mchKey}")
	private String mchKey;

	@Value("#{wxPayProperties.subAppId}")
	private String subAppId;

	@Value("#{wxPayProperties.subMchId}")
	private String subMchId;

	@Value("#{wxPayProperties.keyPath}")
	private String keyPath;

	@Value("#{wxPayProperties.appname}")
	private String appname;

	@Value("#{wxPayProperties.asynNotifyUrl}")
	private String asynNotifyUrl;

	@Value("#{wxPayProperties.recommandfee}")
	private String recommandfee;

	public String getRecommandfee() {
		return recommandfee;
	}

	public void setRecommandfee(String recommandfee) {
		this.recommandfee = recommandfee;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getMchKey() {
		return mchKey;
	}

	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getAsynNotifyUrl() {
		return asynNotifyUrl;
	}

	public void setAsynNotifyUrl(String asynNotifyUrl) {
		this.asynNotifyUrl = asynNotifyUrl;
	}

	@Bean
	public WxPayConfig payConfig() {
		WxPayConfig payConfig = new WxPayConfig();
		payConfig.setAppId(this.appId);
		payConfig.setMchId(this.mchId);
		payConfig.setMchKey(this.mchKey);
		payConfig.setSubAppId(this.subAppId);
		payConfig.setSubMchId(this.subMchId);
		payConfig.setKeyPath(this.keyPath);

		return payConfig;
	}

	@Bean
	public WxPayService payService() {
		WxPayService payService = new WxPayServiceImpl();
		payService.setConfig(payConfig());
		return payService;
	}
}
