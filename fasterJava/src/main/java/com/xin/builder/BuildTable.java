package com.xin.builder;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.xin.bean.Constant;
import com.xin.bean.TableInfo;
import com.xin.utils.PropertiesUtils;
import com.xin.utils.stringUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

    public static void getTables() {
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
                System.out.println(tableInfo.getTableName() + tableInfo.getBeanName() + tableInfo.getComment() + tableInfo.getBeanParamName());
            }
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
    }

    private static String processFiled(String fieldName, Boolean upperCaseFirstLetter) {
        StringBuilder sb = new StringBuilder();
        String[] fields = fieldName.split("_");
        sb.append(upperCaseFirstLetter ? stringUtils.upperFirst(fields[0]) : fields[0]);
        for (int i = 1; i < fields.length; i++) {
            sb.append(stringUtils.upperFirst(fields[i]));
        }
        return sb.toString();
    }
}
