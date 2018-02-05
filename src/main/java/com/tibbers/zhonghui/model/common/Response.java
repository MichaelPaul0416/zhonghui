package com.tibbers.zhonghui.model.common;

/**
 * @author: Paul
 * @time:2018/1/20 14:04
 * @description:
 */
public class Response {

    private boolean serviceStatus;

    private Object responseBody;

    @Override
    public String toString() {
        return "Response{" +
                "serviceStatus=" + serviceStatus +
                ", responseBody=" + responseBody +
                '}';
    }

    public boolean isServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(boolean serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }

    public Response(boolean serviceStatus, Object responseBody) {

        this.serviceStatus = serviceStatus;
        this.responseBody = responseBody;
    }
}
