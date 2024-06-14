package com.xin.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 11:37
 */
public class stringUtils {
    /**
     * 转化成大写
     * @param field
     * @return
     */
    public static String upperFirst(String field) {
        if (StringUtils.isEmpty(field)) {
            return field;
        }
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    /**
     * 转换成小写
     * @param field
     * @return
     */
    public static String lowerFirst(String field) {
        if (StringUtils.isEmpty(field)) {
            return field;
        } else {
            return field.substring(0, 1).toLowerCase() + field.substring(1);
        }
    }

}
