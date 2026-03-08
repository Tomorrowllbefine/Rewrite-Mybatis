package com.kk.mybatis.mapping;

import com.kk.mybatis.session.Configuration;

/**
 * 映射语句
 * 
 * @author limoukun
 * @since 2025/11/3
 */
public class MappedStatement {
    // todo 为什么需要？
    private Configuration configuration;
    /**
     * mapper接口方法名
     */
    private String id;
    /** sql命令类型 **/
    private SqlCommandType sqlCommandType;
    /** 绑定SQl */
    private BoundSql boundSql;

    MappedStatement() {
        // constructor disabled
    }

    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, BoundSql boundSql) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }


    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }
}