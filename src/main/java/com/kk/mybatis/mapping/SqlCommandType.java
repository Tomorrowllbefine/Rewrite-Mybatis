package com.kk.mybatis.mapping;

/**
 * SQL 指令类型
 *
 * @author limoukun
 * @since 2025/11/3
 **/
public enum SqlCommandType {
    /**
     * 未知类型
     */
    UNKNOWN,
    /**
     * 查询操作
     */
    SELECT,
    /**
     * 插入操作
     */
    INSERT,
    /**
     * 更新操作
     */
    UPDATE,
    /**
     * 删除操作
     */
    DELETE;
}
