package com.kk.mybatis.session;

/**
 * SqlSession工厂类
 *
 * @author limoukun
 * @since 2025/10/26
 **/
public interface SqlSessionFactory {

    SqlSession openSession();
}
