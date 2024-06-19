package com.xin.builder;

import cn.hutool.core.io.FileUtil;
import com.xin.bean.Constant;
import com.xin.bean.TableInfo;

import java.io.File;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/18 21:13
 */
public class BuildPo {
    public static void execute(TableInfo tableInfo) {
        File folder = new File(Constant.PATH_PO);
        System.out.println(Constant.PATH_PO);
        //不存在直接创建对应的文件夹
        if (!FileUtil.exist(folder)) {
            FileUtil.mkdir(folder);
        }
        File file = new File(folder, tableInfo.getBeanName() + ".java");
        FileUtil.touch(file);
    }

}
