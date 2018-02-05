package com.tibbers.zhonghui.model.common;

/**
 * @author: Paul
 * @time:2018/1/20 13:59
 * @description:
 */
public class APIResponse {


    private String status;//此次请求的响应状态[0000成功，其他失败]

    private String describe;

    private Response body;

    @Override
    public String toString() {
        return "APIResponse{" +
                "status='" + status + '\'' +
                ", describe='" + describe + '\'' +
                ", body=" + body +
                '}';
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Response getBody() {
        return body;
    }

    public void setBody(Response body) {
        this.body = body;
    }

    public APIResponse(String status, String describe, Response body) {

        this.status = status;
        this.describe = describe;
        this.body = body;
    }
}
