package com.kk.mybatis.session;

import java.util.HashMap;
import java.util.Map;

import com.kk.mybatis.binding.MapperRegistry;
import com.kk.mybatis.mapping.MappedStatement;

/**
 * 配置项
 *
 * @author limoukun
 * @since 2025/10/26
 **/
public class Configuration {
    /**
     * Mapper 注册中心
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * MapperStatement 注册中心
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * MapperRegistry 操作
     */
    public <T> void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }

}