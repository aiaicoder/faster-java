package com.xin.builder;

import com.xin.bean.Constant;
import com.xin.utils.DateUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Date;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/22 20:24
 */
public class BuildComment {
    public static void createClassComment(BufferedWriter bw, String classComment) throws IOException {
        bw.write("/**");
        bw.newLine();
        bw.write(" * @Description:" + classComment);
        bw.newLine();
        bw.write(" * @author " + Constant.AUTHOR);
        bw.newLine();
        bw.write(" * @date:" + DateUtils.format(new Date(), DateUtils.YYYY_MM_DD));
        bw.newLine();
        bw.write(" */");
        bw.newLine();
    }

    public static void createFieldComment(BufferedWriter bw, String fieldComment) throws IOException {
        bw.write("\t/**");
        bw.newLine();
        bw.write("\t * " + (fieldComment == null ? " " : fieldComment));
        bw.newLine();
        bw.write("\t */");
        bw.newLine();

    }

    public static void createMethodComment(String methodName, String comment) {
        StringBuilder sb = new StringBuilder();
        sb.append("/**\n");
        sb.append(" * ").append(comment).append("\n");
        sb.append(" */\n");
        sb.append("public void ").append(methodName).append("() {\n\n");
        sb.append("}");
        System.out.println(sb.toString());
    }
}
