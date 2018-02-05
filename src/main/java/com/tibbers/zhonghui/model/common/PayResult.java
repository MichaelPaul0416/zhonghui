package com.tibbers.zhonghui.model.common;

/**
 * @author: Paul
 * @time:2018/2/3 14:53
 * @description:
 */
public class PayResult {
    private String return_code;
    private String result_code;
    private String appid;
    private String mchid;
    private String nonce_str;
    private String sign;
    private String err_code;
    private String err_code_msg;
    private String trade_type;
    private String prepay_id;
    private String orderid;

    @Override
    public String toString() {
        return "PayResult{" +
                "return_code='" + return_code + '\'' +
                ", result_code='" + result_code + '\'' +
                ", appid='" + appid + '\'' +
                ", mchid='" + mchid + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", sign='" + sign + '\'' +
                ", err_code='" + err_code + '\'' +
                ", err_code_msg='" + err_code_msg + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", orderid='" + orderid + '\'' +
                '}';
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public PayResult(){}

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getErr_code() {
        return err_code;
    }

    public void setErr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_msg() {
        return err_code_msg;
    }

    public void setErr_code_msg(String err_code_msg) {
        this.err_code_msg = err_code_msg;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }
}
