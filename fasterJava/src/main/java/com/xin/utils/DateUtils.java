package com.xin.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/22 20:36
 */
public class DateUtils {
    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static String format(Date date, String patten) {
        return new SimpleDateFormat(patten).format(date);
    }

    public static Date parse(String date, String patten) {
        try {
            return new SimpleDateFormat(patten).parse(date);
        } catch (Exception e) {
            return null;
        }
    }
}
