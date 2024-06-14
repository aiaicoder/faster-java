package com.xin.bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 11:00
 */
public class TableInfo {
    /**
     * 表名
     */
    private String tableName;

    /**
     * bean名称
     */
    private String beanName;

    /**
     * 参数名称
     */
    private String beanParamName;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 字段信息
     */
    private List<FieldInfo> fieldList;

    /**
     * 唯一索引集合
     * 使用LinkedHashMap保证索引的有序性
     */
    private Map<String,List<FieldInfo>> keyIndexMap = new LinkedHashMap();

    /**
     * 是否有DATE类型
     */
    private boolean havaDate;

    /**
     * 是否有时间类型
     */
    private boolean havaDateTime;

    /**
     * 是否含有bigdemical类型
     */
    private boolean havaBigDecimal;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanParamName() {
        return beanParamName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<FieldInfo> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<FieldInfo> fieldList) {
        this.fieldList = fieldList;
    }

    public Map<String, List<FieldInfo>> getKeyIndexMap() {
        return keyIndexMap;
    }

    public void setKeyIndexMap(Map<String, List<FieldInfo>> keyIndexMap) {
        this.keyIndexMap = keyIndexMap;
    }

    public boolean isHavaDate() {
        return havaDate;
    }

    public void setHavaDate(boolean havaDate) {
        this.havaDate = havaDate;
    }

    public boolean isHavaDateTime() {
        return havaDateTime;
    }

    public void setHavaDateTime(boolean havaDateTime) {
        this.havaDateTime = havaDateTime;
    }

    public boolean isHavaBigDecimal() {
        return havaBigDecimal;
    }

    public void setHavaBigDecimal(boolean havaBigDecimal) {
        this.havaBigDecimal = havaBigDecimal;
    }
}
