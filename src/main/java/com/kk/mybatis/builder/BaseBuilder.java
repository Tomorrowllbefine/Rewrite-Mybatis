package com.kk.mybatis.builder;

import com.kk.mybatis.session.Configuration;

/**
 * 基础构建器
 *
 * @author limoukun
 * @since 2025/11/3
 **/
public class BaseBuilder {
    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
