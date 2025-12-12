package com.kk.mybatis.session.defaults;

import com.kk.mybatis.binding.MapperRegistry;
import com.kk.mybatis.session.Configuration;
import com.kk.mybatis.session.SqlSession;

import java.util.Arrays;

/**
 * 默认SqlSession实现类
 *
 * @author limoukun
 * @since 2025/10/26
 **/
public class DefaultSqlSession implements SqlSession {

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
        return (T) ("代理逻辑" + statement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        T result = (T) ("代理逻辑, 方法: " + statement + ", 入参: " + Arrays.toString((Object[]) parameter));
        return result;
    }
}
