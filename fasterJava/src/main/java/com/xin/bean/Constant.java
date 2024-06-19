package com.xin.bean;

import com.xin.utils.PropertiesUtils;
import jdk.internal.org.objectweb.asm.tree.MultiANewArrayInsnNode;

import java.io.File;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 11:20
 */
public class Constant {
    /**
     * 是否忽略表前缀
     */
    public static boolean ignore_table_prefix;
    /**
     * bean参数后缀
     */
    public static String SUFFIX_BEAN_PARAM;

    /**
     * 文件输出路径
     */
    public static String PATH_BASE;

    /**
     * 包名
     */
    public static String PACKAGE_BASE;

    /**
     * po包名
     */
    public static String PATH_PO;

    public static String PATH_JAVA = "java";

    public static String PATH_RESOURCE = "resources";

    public static String PACKAGE_PO;


    static {
        ignore_table_prefix = Boolean.parseBoolean(PropertiesUtils.getProperty("ignore.table.prefix"));
        SUFFIX_BEAN_PARAM = PropertiesUtils.getProperty("suffix.bean.param");
        PATH_BASE = PropertiesUtils.getProperty("path.base");
        PACKAGE_BASE = PropertiesUtils.getProperty("package.base");
        PATH_BASE = PATH_BASE + PATH_JAVA + '/' + PACKAGE_BASE;
        PATH_BASE = PATH_BASE.replace(".", "/");
        PATH_PO = PATH_BASE + "/" + PropertiesUtils.getProperty("package.po").replace(".","/");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.po");
    }

    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public final static String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float"};
    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPES = new String[]{"int",  "tinyint","int unsigned"};
    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};

    public static void main(String[] args) {
        System.out.println(PACKAGE_PO);
    }


}
