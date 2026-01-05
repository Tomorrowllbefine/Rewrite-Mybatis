package com.kk.mybatis.mapping;

import com.kk.mybatis.session.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    /** sql **/
    private String sql;
    /** sql命令类型 **/
    private SqlCommandType sqlCommandType;
    /** 参数类型 **/
    private String parameterType;
    /** 结果类型 **/
    private String resultType;
    /** 参数占位列表 **/
    private Map<Integer, String> parameter;

    MappedStatement() {
        // constructor disabled
    }

    /**
     * 建造者
     */
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, String parameterType,
                       String resultType, String sql, Map<Integer, String> parameter) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameter = parameter;
        }

        public Builder(Configuration configuration, String id, SqlCommandType sqlCommandType, String parameterType,
                       String resultType, String sql) {
            mappedStatement.configuration = configuration;
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.parameterType = parameterType;
            mappedStatement.resultType = resultType;
            mappedStatement.sql = sql;
            mappedStatement.parameter = parseParameter(sql);
        }

        public MappedStatement build() {
            assert mappedStatement.configuration != null;
            assert mappedStatement.id != null;
            return mappedStatement;
        }

        /**
         * 解析参数占位符
         *
         * @param sql sql语句
         * @return 参数占位符列表
         */
        private Map<Integer, String> parseParameter(String sql) {
            Map<Integer, String> parameter = new HashMap<>();
            Pattern pattern = Pattern.compile("(#\\{(.*?)})");
            Matcher matcher = pattern.matcher(sql);
            for (int i = 1; matcher.find(); i++) {
                parameter.put(i, matcher.group(2));
                sql = sql.replace(matcher.group(1), "?");
            }
            return parameter;
        }
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