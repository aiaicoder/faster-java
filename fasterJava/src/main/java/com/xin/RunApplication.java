package com.xin;

import com.xin.bean.TableInfo;
import com.xin.builder.*;

import java.util.List;

/**
 * @author <a href="https://github.com/aiaicoder">  小新
 * @version 1.0
 * @date 2024/6/14 10:48
 */
public class RunApplication {
    public static void main(String[] args) {
        List<TableInfo> tables = BuildTable.getTables();
        BuildBase.build();
        assert tables != null;
        for (TableInfo table : tables) {
            BuildPo.execute(table);
            BuildQuery.execute(table);
            BuildMapper.execute(table);
        }

    }
}
