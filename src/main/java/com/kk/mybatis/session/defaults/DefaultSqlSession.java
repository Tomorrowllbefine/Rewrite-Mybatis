package com.kk.mybatis.session.defaults;

import com.kk.mybatis.mapping.BoundSql;
import com.kk.mybatis.mapping.Environment;
import com.kk.mybatis.mapping.MappedStatement;
import com.kk.mybatis.session.Configuration;
import com.kk.mybatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 默认SqlSession实现类
 *
 * @author limoukun
 * @since 2025/10/26
 **/
public class DefaultSqlSession implements SqlSession {

    private Logger logger = LoggerFactory.getLogger(DefaultSqlSession.class);

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        BoundSql boundSql = mappedStatement.getBoundSql();
        return (T) ("代理逻辑" + statement + ",\n待执行sql: " + boundSql.getSql());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        Environment environment = configuration.getEnvironment();
        BoundSql boundSql = mappedStatement.getBoundSql();
        try (
                Connection connection = environment.getDataSource().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(boundSql.getSql());
        ) {
            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
            // 执行sql，获取结果集
            ResultSet resultSet = preparedStatement.executeQuery();

            // 处理结果集
            List<T> resultList = new ArrayList<>();
            Class<?> resultMetaDataClazz = Class.forName(boundSql.getResultType());
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                T result = (T) resultMetaDataClazz.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethodStr = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp) {
                        method = resultMetaDataClazz.getMethod(setMethodStr, Date.class);
                    } else {
                        method = resultMetaDataClazz.getMethod(setMethodStr, value.getClass());
                    }
                    method.invoke(result, value);
                }
                resultList.add(result);
            }

            logger.info("查询结果: {}", resultList);

            return resultList.get(0);

        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException |
                 NoSuchMethodException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }
}
