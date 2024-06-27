package com.xin.builder;

import cn.hutool.core.io.FileUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.xin.bean.Constant;
import com.xin.bean.FieldInfo;
import com.xin.bean.TableInfo;
import com.xin.utils.stringUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/18 21:13
 */
public class BuildMapper {
    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);

    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constant.PATH_MAPPER);
//        System.out.println(Constant.PATH_PO);
        //不存在直接创建对应的文件夹
        if (!FileUtil.exist(folder)) {
            FileUtil.mkdir(folder);
        }
        String className = tableInfo.getBeanName() + Constant.SUFFIX_MAPPER;
        File file = new File(folder, className + ".java");
        FileUtil.touch(file);
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;
        try {
            out = new FileOutputStream(file);
            outw = new OutputStreamWriter(out);
            bw = new BufferedWriter(outw);
            bw.write("package " + Constant.PACKAGE_MAPPER + ";");
            bw.newLine(); //换行
            bw.write("import java.io.Serializable;");
            bw.newLine();
            bw.write("import org.apache.ibatis.annotations.Param;");
            bw.newLine();
            //构建注释类
            BuildComment.createClassComment(bw, tableInfo.getComment() + "Mapper");
            bw.newLine();
            bw.write(String.format("public interface " + className + "<T, P> extends BaseMapper {", tableInfo.getBeanName()));
            bw.newLine();
            Map<String, List<FieldInfo>> keyIndexMap = tableInfo.getKeyIndexMap();

            for (Map.Entry<String, List<FieldInfo>> entry : keyIndexMap.entrySet()) {
                List<FieldInfo> fieldInfos = entry.getValue();
                int index = 0;
                StringBuilder methodName = new StringBuilder();
                StringBuilder methodParam = new StringBuilder();
                for (FieldInfo fieldInfo : fieldInfos) {
                    index++;
                    methodName.append(stringUtils.upperFirst(fieldInfo.getFieldName()));
                    //如果大于说明是联合索引
                    if (index < fieldInfos.size()) {
                        methodName.append("And");
                    }
                    methodParam.append("@Param(\"").append(fieldInfo.getPropertyName()).append("\")").append(fieldInfo.getJavaType()).append(" ").append(fieldInfo.getPropertyName());
                    //多个参数逗号隔开
                    if (index < fieldInfos.size()) {
                        methodParam.append(", ");
                    }
                }

                BuildComment.createFieldComment(bw, "根据" + methodName + "查询");
                bw.newLine();
                bw.write("\t T selectBy" + methodName + "(" + methodParam + ");");
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据" + methodName + "更新");
                bw.write("\t Integer updateBy" + methodName + "(@Param(\"bean\")T t, " + methodParam + ");");
                bw.newLine();
                BuildComment.createFieldComment(bw, "根据" + methodName + "删除");
                bw.write("\t Integer deleteBy" + methodName + "();");
                bw.newLine();


            }
            bw.newLine();
            bw.write("}");
            bw.flush();
        } catch (Exception e) {
            logger.error("写入mapper失败", e);
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
