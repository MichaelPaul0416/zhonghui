package com.tibbers.zhonghui.utils;

import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author Tibbers
 * @create 2017-06-08 0:59
 */

public class DateUtil {

    private static Logger logger = Logger.getLogger(DateUtil.class);

    /**
     * 获取当前的时间
     *
     * @param dateFormat
     * @return
     */
    public static String getTime(String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        Date date = new Date();
        return df.format(date.getTime());
    }


    /**
     * 转换日期格式
     * @param time
     * @param from
     * @param to
     * @return
     * @throws ParseException
     */
    public static String formatTime(String time, String from, String to) throws ParseException {
        DateFormat df = new SimpleDateFormat(from);
        Date date = df.parse(time);
        df = new SimpleDateFormat(to);
        return df.format(date);
    }

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(String stamp,String format){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        long lt = new Long(stamp);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


}
