package com.kk.mybatis.mapping;

import com.kk.mybatis.transaction.TransactionFactory;

import javax.sql.DataSource;

/**
 * SQL环境
 *
 * @author limoukun
 * @since 2026/3/4
 **/
public final class Environment {
    /** 环境唯一标识 */
    private final String id;
    /** 事务工厂，用于创建事务 */
    private final TransactionFactory transactionFactory;
    /** 数据源，用于获取数据库连接 */
    private final DataSource dataSource;

    public Environment(String id, TransactionFactory transactionFactory, DataSource dataSource) {
        this.id = id;
        this.transactionFactory = transactionFactory;
        this.dataSource = dataSource;
    }

    public static class Builder {
        private String id;
        private TransactionFactory transactionFactory;
        private DataSource dataSource;

        public Builder(String id) {
            this.id = id;
        }

        public Builder transactionFactory(TransactionFactory transactionFactory) {
            this.transactionFactory = transactionFactory;
            return this;
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Environment build() {
            return new Environment(id, transactionFactory, dataSource);
        }
    }

    public String getId() {
        return id;
    }

    public TransactionFactory getTransactionFactory() {
        return transactionFactory;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}
