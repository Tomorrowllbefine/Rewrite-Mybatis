package com.kk.mybatis.session;

import java.util.Collection;
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

    /**
     * 添加MappedStatement
     *
     * @param mappedStatement MappedStatement对象
     */
    public void addMappedStatement(MappedStatement mappedStatement) {
        mappedStatements.put(mappedStatement.getId(), mappedStatement);
    }

    /**
     * 根据ID获取MappedStatement
     *
     * @param id MappedStatement的ID
     * @return MappedStatement对象
     */
    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    /**
     * 获取所有MappedStatement
     *
     * @return MappedStatement集合
     */
    public Collection<MappedStatement> getMappedStatements() {
        return mappedStatements.values();
    }
}