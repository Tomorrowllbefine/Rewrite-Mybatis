package com.kk.mybatis.transaction.jdbc;

import com.kk.mybatis.session.TransactionIsolationLevel;
import com.kk.mybatis.transaction.Transaction;
import com.kk.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Jdbc事务工厂
 *
 * @author limoukun
 * @since 2026/2/3
 **/
public class JdbcTransactionFactory implements TransactionFactory {
    @Override
    public Transaction newTransaction(Connection conn) {
        return new JdbcTransaction(conn);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
