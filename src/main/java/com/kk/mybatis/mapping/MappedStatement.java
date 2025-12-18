package com.kk.mybatis.mapping;

/**
 * 映射语句
 * 
 * @author limoukun
 * @since 2025/11/3
 */
public class MappedStatement {
    
    private String id;
    private String sql;
    private SqlCommandType sqlCommandType;

    public MappedStatement(String id, String sql, SqlCommandType sqlCommandType) {
        this.id = id;
        this.sql = sql;
        this.sqlCommandType = sqlCommandType;
    }

    public String getId() {
        return id;
    }

    public String getSql() {
        return sql;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }
}