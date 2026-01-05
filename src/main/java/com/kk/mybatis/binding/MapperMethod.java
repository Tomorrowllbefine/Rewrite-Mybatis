package com.kk.mybatis.binding;

import com.kk.mybatis.mapping.MappedStatement;
import com.kk.mybatis.mapping.SqlCommandType;
import com.kk.mybatis.session.Configuration;
import com.kk.mybatis.session.SqlSession;

import java.lang.reflect.Method;

/**
 * Mapper方法
 *
 * @author limoukun
 * @since 2026/1/4
 **/
public class MapperMethod {
    /**
     * SQL 命令
     **/
    private final SqlCommand command;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
    }

    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        switch (this.command.getType()) {
            case INSERT:
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
            case SELECT:
                result = sqlSession.selectOne(command.getName(), args);
                break;
            default:
                throw new RuntimeException("Unknown Execution method for: " + command.getName());
        }
        return result;
    }

    /**
     * SQL 指令
     **/
    public static class SqlCommand {
        /**
         * 映射语句id
         * <p>例如：com.kk.mybatis.binding.dao.IUserDao.queryUserName
         **/
        private final String name;
        /**
         * SQL 命令类型
         **/
        private final SqlCommandType type;

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement mappedStatement = configuration.getMappedStatement(statementName);
            name = mappedStatement.getId();
            type = mappedStatement.getSqlCommandType();
        }

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }
    }
}
