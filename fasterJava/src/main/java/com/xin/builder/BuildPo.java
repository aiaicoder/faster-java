package com.xin.builder;

import cn.hutool.core.io.FileUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.xin.bean.Constant;
import com.xin.bean.FieldInfo;
import com.xin.bean.TableInfo;
import com.xin.utils.DateUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/18 21:13
 */
public class BuildPo {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constant.PATH_PO);
//        System.out.println(Constant.PATH_PO);
        //不存在直接创建对应的文件夹
        if (!FileUtil.exist(folder)) {
            FileUtil.mkdir(folder);
        }
        File file = new File(folder, tableInfo.getBeanName() + ".java");
        FileUtil.touch(file);
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            outw = new OutputStreamWriter(out);
            bw = new BufferedWriter(outw);
            bw.write("package " + Constant.PACKAGE_PO + ";");
            bw.newLine(); //换行
            bw.write("import java.io.Serializable;");
            bw.newLine();
            //引入lombok注解
            bw.write("import lombok.Data;");
            if (tableInfo.isHavaDate() || tableInfo.isHavaDateTime()) {
                bw.write("import java.util.Date;");
                bw.newLine();
                bw.write(Constant.BEAN_DATA_FORMAT_CLASS);
                bw.newLine();
                bw.write(Constant.BEAN_DATA_PARSE_CLASS);
                bw.newLine();
            }
            if (tableInfo.isHavaBigDecimal()) {
                bw.write("import java.math.BigDecimal;");
                bw.newLine();
            }
            boolean isIgnore = false;
            //只要判断需要有忽略字段，就直接结束循环，节省时间
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
               if (ArrayUtils.contains(Constant.IGNORE_FORMAT_FIELDS,fieldInfo.getPropertyName())){
                   isIgnore = true;
                   break;
               }
            }
            if (isIgnore){
                bw.write(Constant.IGNORE_BEAN_TOJSON_CLASS);
                bw.newLine();
            }

            bw.newLine();
            bw.newLine();
            //构建注释类
            BuildComment.createClassComment(bw, tableInfo.getComment());
            bw.newLine();
            bw.write("@Data");
            bw.newLine();
            bw.write("public class " + tableInfo.getBeanName() + " implements Serializable {");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                if (ArrayUtils.contains(Constant.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t"+String.format(Constant.BEAN_DATA_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD_HH_MM_SS));
                    bw.newLine();
                }
                if (ArrayUtils.contains(Constant.SQL_DATE_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t"+String.format(Constant.BEAN_DATA_FORMAT_EXPRESSION, DateUtils.YYYY_MM_DD));
                    bw.newLine();
                }
                if (ArrayUtils.contains(Constant.IGNORE_FORMAT_FIELDS, fieldInfo.getPropertyName())) {
                    bw.write("\t"+String.format(Constant.IGNORE_BEAN_TOJSON_EXPRESSION, fieldInfo.getPropertyName()));
                    bw.newLine();
                }
                bw.write("\t private " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
            }
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("写入文件失败", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    logger.error("写入文件失败", e);
                }
            }
            if (outw != null) {
                try {
                    outw.close();
                } catch (IOException e) {
                    logger.error("写入文件失败", e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("写入文件失败", e);
                }
            }
        }

    }

}
