package com.kk.mybatis.session.defaults;

import com.kk.mybatis.session.Configuration;
import com.kk.mybatis.session.SqlSession;
import com.kk.mybatis.session.SqlSessionFactory;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;
    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}