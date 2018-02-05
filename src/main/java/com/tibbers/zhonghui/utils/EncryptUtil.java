package com.tibbers.zhonghui.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author Tibbers
 * @create 2017-10-16 0:57
 */

public class EncryptUtil {
    /**
     * 用MD5算法进行加密
     * @param str 需要加密的字符串
     * @return MD5加密后的结果
     */
    public static String encodeMD5String(String str) {
        return encode(str, "MD5");
    }

    /**
     * 用SHA算法进行加密
     * @param str 需要加密的字符串
     * @return SHA加密后的结果
     */
    public static String encodeSHAString(String str) {
        return encode(str, "SHA");
    }



    private static String encode(String str, String method) {
        MessageDigest md = null;
        String dstr = null;
        try {
            md = MessageDigest.getInstance(method);
            md.update(str.getBytes());
            dstr = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dstr.toUpperCase();
    }

    //密钥需要一起传送进来
    public static String contactParams(Map<String,String> params){
        if(params != null && params.entrySet().size() > 0){
            Set<Map.Entry<String,String>> entries = params.entrySet();
            Iterator<Map.Entry<String,String>> iterator = entries.iterator();
            StringBuilder builder = new StringBuilder();
            while (iterator.hasNext()){
                Map.Entry<String,String> entry = iterator.next();
                if(StringUtils.isNoneEmpty(entry.getValue())){
                    builder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
            }
            String paramsStr = builder.toString();
            return paramsStr.substring(0,paramsStr.length());
        }else{
            return "";
        }
    }
}
