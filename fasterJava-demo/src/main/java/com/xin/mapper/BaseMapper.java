package com.xin.mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author <a href="https://github.com/aiaicoder">
 * @version 1.0
 * @date 2024/6/27 19:55
 */
interface BaseMapper<T,P> {
    /**
     * 插入
     */
    Integer insert(@Param("bean") T t);

    /**
     * 更新或者插入
     */
    Integer insertOrUpdate(@Param("bean") T t);

    /**
     * 批量插入
     */
    Integer insertBatch(@Param("list") List<T> list);

    /**
     * 批量插入或者更新
     */
    Integer insertOrUpdateBatch(@Param("list") List<T> list);

    /**
     * 根据参数查询集合
     */
    List<T> selectList(@Param("bean") P p);

    /**
     * 根据集合查询数量
     */
    Integer selectCount(@Param("bean") P p);

}
