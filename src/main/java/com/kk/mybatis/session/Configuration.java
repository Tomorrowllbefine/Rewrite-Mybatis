package com.kk.mybatis.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.kk.mybatis.binding.MapperRegistry;
import com.kk.mybatis.mapping.Environment;
import com.kk.mybatis.mapping.MappedStatement;
import com.kk.mybatis.session.datasource.druid.DruidDataSourceFactory;
import com.kk.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.kk.mybatis.type.TypeAliasRegistry;

/**
 * 配置项
 *
 * @author limoukun
 * @since 2025/10/26
 **/
public class Configuration {

    /**
     * 环境
     */
    protected Environment environment;
    /**
     * 按环境 ID 缓存所有解析出来的 Environment，便于后续按需切换
     */
    protected final Map<String, Environment> environments = new HashMap<>();
    /**
     * Mapper 注册中心
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * MapperStatement 注册中心
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 类型别名注册中心
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
    }

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

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    /**
     * 添加一个环境配置，并按 ID 建立索引
     *
     * @param environment 环境对象
     */
    public void addEnvironment(Environment environment) {
        if (environment == null) {
            return;
        }
        environments.put(environment.getId(), environment);
    }

    /**
     * 获取当前生效环境
     *
     * @return 当前环境
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * 设置当前生效环境
     *
     * @param environment 当前环境
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 按 ID 获取环境
     *
     * @param id 环境 ID
     * @return 环境对象
     */
    public Environment getEnvironment(String id) {
        return environments.get(id);
    }
}
