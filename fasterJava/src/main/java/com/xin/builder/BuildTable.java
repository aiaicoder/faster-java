package com.xin.builder;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.xin.bean.Constant;
import com.xin.bean.FieldInfo;
import com.xin.bean.TableInfo;
import com.xin.utils.PropertiesUtils;
import com.xin.utils.stringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 10:35
 * 通过jdbc链接数据库
 */
public class BuildTable {
    //创建链接
    private static Connection conn = null;
    //引入日志
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);

    private static final String SQL_SHOW_TABLES = "show table status";

    private static final String SQL_SHOW_FIELDS = "show full fields from %s";
    private static final String SQL_SHOW_KEY_INDEX = "show index from %s";

    static {
        String driverName = PropertiesUtils.getProperty("db.driver.name");
        String url = PropertiesUtils.getProperty("db.url");
        String username = PropertiesUtils.getProperty("db.username");
        String password = PropertiesUtils.getProperty("db.password");
        try {
            Class.forName(driverName);
            conn = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("数据库连接失败", e);
        }
    }

    public static List<TableInfo> getTables() {
        PreparedStatement ps = null;
        ResultSet tableResult = null;
        try {
            ps = conn.prepareStatement(SQL_SHOW_TABLES);
            tableResult = ps.executeQuery();
            List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
            while (tableResult.next()) {
                String tableName = tableResult.getString("name");
                String comment = tableResult.getString("comment");
                String beanName = tableName;
                if (Constant.ignore_table_prefix) {
                    //如何表名有没有下划线仅大写首字母
                    if (!beanName.contains("_")) {
                        beanName = stringUtils.upperFirst(beanName);
                    } else {
                        beanName = tableName.substring(beanName.indexOf("_") + 1);
                    }
                }
                beanName = processFiled(beanName, true);
                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setBeanName(beanName);
                tableInfo.setComment(comment);
                tableInfo.setBeanParamName(beanName + Constant.SUFFIX_BEAN_PARAM);
                readFieldInfo(tableInfo);
                //获取索引信息
                getKeyInfo(tableInfo);
                tableInfoList.add(tableInfo);
            }
            return tableInfoList;
        } catch (Exception e) {
            logger.error("获取表失败", e);
        } finally {
            if (tableResult != null) {
                try {
                    tableResult.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 读取表中字段信息
     *
     * @param tableInfo
     * @return
     */
    private static void readFieldInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet filedResult = null;
        List<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();
        try {
            ps = conn.prepareStatement(String.format(SQL_SHOW_FIELDS, tableInfo.getTableName()));
            filedResult = ps.executeQuery();

            while (filedResult.next()) {
                String fieldName = filedResult.getString("field");
                String type = filedResult.getString("type");
                String extra = filedResult.getString("extra");
                String comment = filedResult.getString("comment");
                //去除掉自带的括号
                if (type.indexOf("(") > 0) {
                    type = type.substring(0, type.indexOf("("));
                }
                String propertyName = processFiled(fieldName, false);
                FieldInfo fieldInfo = new FieldInfo();
                fieldInfoList.add(fieldInfo);
                fieldInfo.setFieldName(fieldName);
                fieldInfo.setSqlType(type);
                fieldInfo.setAutoIncrement("auto_increment".equalsIgnoreCase(extra));
                fieldInfo.setpropertyName(propertyName);
                fieldInfo.setJavaType(processJavaType(type));
                //判断是否含有以上类型
                if (ArrayUtils.contains(Constant.SQL_DATE_TIME_TYPES, type)) {
                    tableInfo.setHavaDateTime(true);
                }
                if (ArrayUtils.contains(Constant.SQL_DATE_TYPES, type)) {
                    tableInfo.setHavaDate(true);
                }
                if (ArrayUtils.contains(Constant.SQL_DECIMAL_TYPES, type)) {
                    tableInfo.setHavaBigDecimal(true);
                }
                //获取所有字段的对应索引表
            }
            //将所有字段放入表中
            tableInfo.setFieldList(fieldInfoList);
        } catch (Exception e) {
            logger.error("获取表失败", e);
        } finally {
            if (filedResult != null) {
                try {
                    filedResult.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 读取主键信息
     *
     * @param tableInfo
     * @return
     */
    private static void getKeyInfo(TableInfo tableInfo) {
        PreparedStatement ps = null;
        ResultSet indexKeyResult = null;
        try {
            Map<String, FieldInfo> tempMap = new HashMap<String, FieldInfo>();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                tempMap.put(fieldInfo.getFieldName(), fieldInfo);
            }
            ps = conn.prepareStatement(String.format(SQL_SHOW_KEY_INDEX, tableInfo.getTableName()));
            indexKeyResult = ps.executeQuery();

            while (indexKeyResult.next()) {
                String fieldName = indexKeyResult.getString("column_name");
                String keyName = indexKeyResult.getString("key_name");
                int nonUnique = indexKeyResult.getInt("non_unique"); //为0的时候是唯一索引
                if (nonUnique == 1) {
                    continue;
                }
                Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();
                List<FieldInfo> keyFiledList = keyIndexMap.get(keyName);
                if (keyFiledList == null) {
                    //创建一个list
                    keyFiledList = new ArrayList<FieldInfo>();
                    //对应索引名称对应一个字段的属性
                    keyIndexMap.put(keyName, keyFiledList);
                }
//                for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
//                    if (fieldInfo.getFieldName().equals(fieldName)){
//                        keyFiledList.add(fieldInfo);
//                    }
//                }
                keyFiledList.add(tempMap.get(fieldName));
            }

        } catch (Exception e) {
            logger.error("获取索引失败", e);
        } finally {
            if (indexKeyResult != null) {
                try {
                    indexKeyResult.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 处理字段,返回驼峰式的字段
     *
     * @param fieldName
     * @param upperCaseFirstLetter
     * @return
     */
    private static String processFiled(String fieldName, Boolean upperCaseFirstLetter) {
        StringBuilder sb = new StringBuilder();
        String[] fields = fieldName.split("_");
        sb.append(upperCaseFirstLetter ? stringUtils.upperFirst(fields[0]) : fields[0]);
        for (int i = 1; i < fields.length; i++) {
            sb.append(stringUtils.upperFirst(fields[i]));
        }
        return sb.toString();
    }

    /**
     * 根据不同的数据类型，返回对应的java类型
     *
     * @param type
     * @return
     */
    private static String processJavaType(String type) {
        if (ArrayUtils.contains(Constant.SQL_INTEGER_TYPES, type)) {
            return "Integer";
        } else if (ArrayUtils.contains(Constant.SQL_LONG_TYPES, type)) {
            return "Long";
        } else if (ArrayUtils.contains(Constant.SQL_DATE_TYPES, type) || ArrayUtils.contains(Constant.SQL_DATE_TIME_TYPES, type)) {
            return "Date";
        } else if (ArrayUtils.contains(Constant.SQL_STRING_TYPES, type)) {
            return "String";
        } else if (ArrayUtils.contains(Constant.SQL_DECIMAL_TYPES, type)) {
            return "BigDecimal";
        } else {
            throw new RuntimeException("无法识别类型：" + type);
        }
    }
}
