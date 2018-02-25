package com.tibbers.zhonghui.config;

/**
 * @author: Paul
 * @time:2018/1/20 12:42
 * @description:
 */
public class APIException extends RuntimeException {
    public APIException(String msg){
        super(msg);
    }

    public APIException(String message,Throwable e){
        super(message,e);
    }

    public APIException(Throwable e){
        super(e);
    }
}
