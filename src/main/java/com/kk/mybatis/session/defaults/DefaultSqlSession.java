package com.kk.mybatis.session.defaults;

import com.kk.mybatis.binding.MapperRegistry;
import com.kk.mybatis.session.SqlSession;

import java.util.Arrays;

/**
 * 默认SqlSession实现类
 *
 * @author limoukun
 * @since 2025/10/26
 **/
public class DefaultSqlSession implements SqlSession {

    private final MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }

    @Override
    public <T> T selectOne(String statement) {
        return (T) ("代理逻辑" + statement);
    }

    @Override
    public <T> T selectOne(String statement, Object parameter) {
        return (T) ("代理逻辑, 方法: " + statement + ", 入参: " + Arrays.toString((Object[]) parameter));
    }
}
