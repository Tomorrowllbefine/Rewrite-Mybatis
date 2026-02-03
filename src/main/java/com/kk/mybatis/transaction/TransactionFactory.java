package com.kk.mybatis.transaction;

import com.kk.mybatis.session.TransactionIsolationLevel;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * 事务工厂
 *
 * @author limoukun
 * @since 2026/2/3
 **/
public interface TransactionFactory {

    /**
     * 基于连接创建事务
     *
     * @param conn 数据库连接
     * @return com.kk.mybatis.transaction.Transaction
     **/
    Transaction newTransaction(Connection conn);

    /**
     * 基于数据源和事务隔离级别创建事务
     *
     * @param dataSource 数据源
     * @param level      事务隔离级别
     * @param autoCommit 是否自动提交
     * @return com.kk.mybatis.transaction.Transaction
     **/
    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);
}
