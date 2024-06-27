package com.xin.bean;

import com.xin.utils.PropertiesUtils;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 11:20
 */
public class Constant {


    public static String AUTHOR;
    /**
     * 是否忽略表前缀
     */
    public static boolean IGNORE_TABLE_PREFIX;
    /**
     * bean参数后缀
     */
    public static String SUFFIX_BEAN_QUERY;

    //需要忽略格式化的属性
    public static String[] IGNORE_FORMAT_FIELDS;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;

    public static String PATH_QUERY;

    public static String PACKAGE_QUERY;

    //日期格式化，反序列化
    public static String BEAN_DATA_FORMAT_EXPRESSION;
    public static String BEAN_DATA_FORMAT_CLASS;
    public static String BEAN_DATA_PARSE_EXPRESSION;
    public static String BEAN_DATA_PARSE_CLASS;

    //查询配置
    public static String SUFFIX_BEAN_QUERY_LIKE;
    public static String SUFFIX_BEAN_QUERY_TIME_START;
    public static String SUFFIX_BEAN_QUERY_TIME_END;



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

    public static String PACKAGE_MAPPER;

    public static String PATH_MAPPER;

    public static String SUFFIX_MAPPER;


    static {
        //需要忽略json格式化的字段
        IGNORE_FORMAT_FIELDS = PropertiesUtils.getProperty("ignore.bean.toJson.fields").split(",");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getProperty("ignore.bean.toJson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getProperty("ignore.bean.toJson.class");
        //日期格式化，反序列化
        BEAN_DATA_FORMAT_EXPRESSION = PropertiesUtils.getProperty("bean.data.format.expression");
        BEAN_DATA_FORMAT_CLASS = PropertiesUtils.getProperty("bean.data.format.class");
        BEAN_DATA_PARSE_EXPRESSION = PropertiesUtils.getProperty("bean.data.parse.expression");
        BEAN_DATA_PARSE_CLASS = PropertiesUtils.getProperty("bean.data.parse.class");
        //查询配置
        SUFFIX_BEAN_QUERY_LIKE = PropertiesUtils.getProperty("suffix.bean.query.like");
        SUFFIX_BEAN_QUERY_TIME_START = PropertiesUtils.getProperty("suffix.bean.query.time.start");
        SUFFIX_BEAN_QUERY_TIME_END = PropertiesUtils.getProperty("suffix.bean.query.time.end");

        SUFFIX_MAPPER = PropertiesUtils.getProperty("suffix.mapper");

        //是否忽视前缀
        IGNORE_TABLE_PREFIX = Boolean.parseBoolean(PropertiesUtils.getProperty("ignore.table.prefix"));
        //bean参数后缀
        SUFFIX_BEAN_QUERY = PropertiesUtils.getProperty("suffix.bean.Query");
        //文件输出基础路径
        PATH_BASE = PropertiesUtils.getProperty("path.base");
        //包名
        PACKAGE_BASE = PropertiesUtils.getProperty("package.base");
        //路径拼接
        PATH_BASE = PATH_BASE + PATH_JAVA + '/' + PACKAGE_BASE;
        //替换.
        PATH_BASE = PATH_BASE.replace(".", "/");
        //po路径
        PATH_PO = PATH_BASE + "/" + PropertiesUtils.getProperty("package.po").replace(".", "/");
        PATH_QUERY = PATH_BASE + "/" + PropertiesUtils.getProperty("package.query").replace(".", "/");
        PATH_MAPPER = PATH_BASE + "/" + PropertiesUtils.getProperty("package.mapper").replace(".", "/");
        //po完整包名
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.po");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.query");
        PACKAGE_MAPPER = PACKAGE_BASE + "." + PropertiesUtils.getProperty("package.mapper");
        AUTHOR = PropertiesUtils.getProperty("author.comment");
    }

    public final static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};
    public final static String[] SQL_DATE_TYPES = new String[]{"date"};
    public static final String[] SQL_DECIMAL_TYPES = new String[]{"decimal", "double", "float"};
    public static final String[] SQL_STRING_TYPES = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};
    public static final String[] SQL_INTEGER_TYPES = new String[]{"int",  "tinyint","int unsigned"};
    public static final String[] SQL_LONG_TYPES = new String[]{"bigint"};



}
