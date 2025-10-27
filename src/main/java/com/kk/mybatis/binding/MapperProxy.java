package com.kk.mybatis.binding;

import com.kk.mybatis.session.SqlSession;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 映射器代理类
 *
 * @author limoukun
 * @since 2025/10/14
 **/
public class MapperProxy<T> implements InvocationHandler, Serializable {
    private static final long serialVersionUID = -6424540398559729838L;

    private SqlSession sqlSession;
    private final Class<T> mapperInterface;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface) {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 调用的是 Object 自带的方法（如 toString、hashCode），则直接调用
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        }
        // 调用的是 Mapper 接口的方法（业务方法）， 则拦截调用，执行自定义逻辑
        if (args == null || args.length == 0) {
            return sqlSession.selectOne(method.getName());
        }
        return sqlSession.selectOne(method.getName(), args);
    }
}
