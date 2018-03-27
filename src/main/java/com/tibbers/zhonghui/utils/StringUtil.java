package com.tibbers.zhonghui.utils;

import com.alibaba.fastjson.JSONObject;
import com.tibbers.zhonghui.config.APIException;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Tibbers
 * @create 2017-10-04 1:24
 */

public class StringUtil {
    private static Logger logger = Logger.getLogger(StringUtil.class);

    /**
     * 是否为空字符串
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    /**
     * 正则表达式匹配
     *
     * @param str String
     * @return boolean
     */
    public static boolean isMatch(String str, String sRegExp) {
        Pattern pattern = Pattern.compile(sRegExp);
        Matcher isLegal = pattern.matcher(str);
        return isLegal.matches();
    }

    public static String formatNumber(int number,int targetLength,String replaceChar){
        String numberStr = String.valueOf(number);
        if(targetLength >= numberStr.length()){
            StringBuilder builder = new StringBuilder();
            for(int i=0;i<targetLength-numberStr.length()-3;i++){
                builder.append(replaceChar);
            }
            builder.append(numberStr).append("000");
            return builder.toString();
        }else{
            throw new APIException("格式化后的字符串长度应该大于输入的数字位数");
        }
    }

    public static String generateUUID(){
        int random = (int)(Math.random() * 1000000);
        String uuid = UUID.randomUUID().toString().replaceAll("-","").substring(0,10);
        return uuid + random;
    }

    public static String currentDateTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

    public static String currentDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }

    public static String currentTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HHmmss");
        return simpleDateFormat.format(new Date());
    }

    public static String serialId(){
        String timestamp = currentDateTime();
        return  timestamp + generateUUID();
    }

    public static boolean argsNotEmpty(String[] args){
        boolean flag = true;
        for(String arg : args){
            if(StringUtils.isEmpty(arg)){
                flag = false;
                break;
            }
        }
        return flag;
    }

    public static String formatXMLDate(Object object){
        return "<![CDATA"+ JSONObject.toJSON(object)+"]";
    }

    public static String randomStr(int length){
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-","");
        return uuid.substring(0,length);
    }


    public static double formatStr2Dobule(String amount){
        double num;
        DecimalFormat myformat = new DecimalFormat("#0.00");
        num = Double.parseDouble(amount);//装换为double类型
        num = Double.parseDouble(myformat.format(num));//保留2为小数
        return num;
    }

    public static double multiply(String d1,String d2){
        BigDecimal bigDecimal = new BigDecimal(d1);
        return bigDecimal.multiply(new BigDecimal(d2)).doubleValue();
    }

    public static double multiply(double d1,double d2){
        return multiply(String.valueOf(d1),String.valueOf(d2));
    }

    public static String caculteIncome(double amount,int feePercent){
        double percent = feePercent / 100.0;
        return String.valueOf(formatStr2Dobule(String.valueOf(multiply(amount,percent))));
    }

    public static void main(String args[]){
        System.out.println(caculteIncome(12.2,2));
    }
}

