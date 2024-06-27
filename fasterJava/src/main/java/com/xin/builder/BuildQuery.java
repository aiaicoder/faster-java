package com.xin.builder;

import cn.hutool.core.io.FileUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.xin.bean.Constant;
import com.xin.bean.FieldInfo;
import com.xin.bean.TableInfo;
import org.apache.commons.lang3.ArrayUtils;

import java.io.*;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/27 18:17
 */
public class BuildQuery {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constant.PATH_QUERY);
//        System.out.println(Constant.PATH_QUERY);
        //不存在直接创建对应的文件夹
        if (!FileUtil.exist(folder)) {
            FileUtil.mkdir(folder);
        }
        String className = tableInfo.getBeanName() + Constant.SUFFIX_BEAN_QUERY;
        File file = new File(folder, className + ".java");
        FileUtil.touch(file);
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            outw = new OutputStreamWriter(out);
            bw = new BufferedWriter(outw);
            bw.write("package " + Constant.PACKAGE_QUERY + ";");
            bw.newLine(); //换行
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
            bw.newLine();
            bw.newLine();
            //构建注释类
            BuildComment.createClassComment(bw, tableInfo.getComment());
            bw.newLine();
            bw.write("@Data");
            bw.newLine();
            bw.write("public class " + className + " {");
            bw.newLine();
            for (FieldInfo fieldInfo : tableInfo.getFieldList()) {
                BuildComment.createFieldComment(bw, fieldInfo.getComment());
                bw.write("\t private " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + ";");
                bw.newLine();
                //字符串模糊查询
                if (ArrayUtils.contains(Constant.SQL_STRING_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t private " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constant.SUFFIX_BEAN_QUERY_LIKE + ";");
                    bw.newLine();
                }
                //时间模糊查询
                if (ArrayUtils.contains(Constant.SQL_DATE_TYPES, fieldInfo.getSqlType()) || ArrayUtils.contains(Constant.SQL_DATE_TIME_TYPES, fieldInfo.getSqlType())) {
                    bw.write("\t private String  " + fieldInfo.getPropertyName() + Constant.SUFFIX_BEAN_QUERY_TIME_START + ";");
                    bw.newLine();
                    bw.write("\t private " + fieldInfo.getJavaType() + " " + fieldInfo.getPropertyName() + Constant.SUFFIX_BEAN_QUERY_TIME_END + ";");
                    bw.newLine();
                }
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
