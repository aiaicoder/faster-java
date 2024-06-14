package com.xin.bean;

import com.xin.utils.PropertiesUtils;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 11:20
 */
public class Constant {
    public static boolean ignore_table_prefix;
    public static String SUFFIX_BEAN_PARAM;

    static {
        ignore_table_prefix = Boolean.parseBoolean(PropertiesUtils.getProperty("ignore.table.prefix"));
        SUFFIX_BEAN_PARAM = PropertiesUtils.getProperty("suffix.bean.param");

    }
}
