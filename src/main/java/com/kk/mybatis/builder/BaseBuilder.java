package com.kk.mybatis.builder;

import com.kk.mybatis.session.Configuration;
import com.kk.mybatis.type.TypeAliasRegistry;

/**
 * 基础构建器
 *
 * @author limoukun
 * @since 2025/11/3
 **/
public class BaseBuilder {
    protected final Configuration configuration;
    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
