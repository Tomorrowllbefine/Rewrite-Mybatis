package com.kk.mybatis.binding;

import com.kk.mybatis.session.SqlSession;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 映射器代理类工厂
 * 负责根据传入的 Mapper 接口创建它的代理对象
 *
 * @author limoukun
 * @since 2025/10/18
 **/
public class MapperProxyFactory<T> {
    private final Class<T> mapperInterface;


    public MapperProxyFactory(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @SuppressWarnings("unchecked")
    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface);
        return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class<?>[]{mapperInterface}, mapperProxy);
    }
}
