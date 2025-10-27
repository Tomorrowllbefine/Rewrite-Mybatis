package com.kk.mybatis.session;

/**
 * 定义执行SQL、获取Mapper、事务管理等
 *
 * @author limoukun
 * @since 2025/10/26
 **/
public interface SqlSession {

    /**
     * 根据传入的Mapper接口，创建Mapper接口的实现类
     *
     * @param type Mapper接口的类型
     * @return Mapper接口的实现类
     **/
    <T> T getMapper(Class<T> type);

    /**
     * 根据指定的SqlID获取一条记录的封装对象
     *
     * @param statement 映射文件中的SqlID
     * @return 封装对象
     **/
    <T> T selectOne(String statement);

    /**
     * 根据指定的SqlID和参数获取一条记录的封装对象,
     * 一般，这个参数传递的是pojo，或者Map或者ImmutableMap
     *
     * @param statement 映射文件中的SqlID，唯一
     * @param parameter 传入statement的参数
     * @return 封装对象
     **/
    <T> T selectOne(String statement, Object parameter);
}
