package com.xin.builder;

import cn.hutool.core.io.FileUtil;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import com.xin.bean.Constant;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/27 20:03
 */
public class BuildBase {

    private static final Logger logger = LoggerFactory.getLogger(BuildTable.class);
    public static void build() {
        List<String> headInfos = new ArrayList<>();
        headInfos.add("package " + Constant.PACKAGE_MAPPER + ";");
        execute(headInfos,"BaseMapper", Constant.PATH_MAPPER);
    }

    public static void execute(List<String> headInfos, String fileName, String outPutPath) {
        File folder = new File(outPutPath);
        if (!FileUtil.exist(folder)) {
            FileUtil.mkdir(folder);
        }
        File javaFile = new File(outPutPath, fileName + ".java");
        OutputStream out = null;
        OutputStreamWriter outw = null;
        BufferedWriter bw = null;

        InputStream in = null;
        InputStreamReader inr = null;
        BufferedReader bf = null;

        try {
            out = Files.newOutputStream(javaFile.toPath());
            outw = new OutputStreamWriter(out);
            bw = new BufferedWriter(outw);
            String templatePath = BuildBase.class.getClassLoader().getResource("template/" + fileName + ".txt").getPath();
            //读入内容
            in = Files.newInputStream(Paths.get(templatePath.replaceFirst("/","")));
            inr = new InputStreamReader(in);
            bf = new BufferedReader(inr);
            for (String head : headInfos) {
                bw.write(head);
                bw.newLine();
            }
            String line;
            while ((line = bf.readLine()) != null) {
                bw.write(line);
                bw.newLine();
            }
            bw.flush();


        } catch (Exception e) {
            logger.error("生成文件失败",e);
        }finally {
            try {
                if (Objects.nonNull(bw)) {
                    bw.close();
                }
                if (Objects.nonNull(outw)) {
                    outw.close();
                }
                if (Objects.nonNull(out)) {
                    out.close();
                }
                if (Objects.nonNull(bf)) {
                    bf.close();
                }
                if (Objects.nonNull(inr)) {
                    inr.close();
                }
                if (Objects.nonNull(in)) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("关闭流异常",e);
            }
        }
    }
}
