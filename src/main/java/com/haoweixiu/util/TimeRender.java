package com.haoweixiu.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取系统当前时间（特定格式）
 *
 * @author zhangxiawan
 *
 */
public class TimeRender {

    private static SimpleDateFormat formatBuilder;

    public static String getDate(String format) {
        formatBuilder = new SimpleDateFormat(format);
        return formatBuilder.format(new Date());
    }

    public static String getDate() {
        return getDate("yyyy-MM-dd HH:mm:ss");// 2006-11-17 15:19:56
    }
}
