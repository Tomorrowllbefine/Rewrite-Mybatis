package com.kk.mybatis.session.datasource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 数据源工厂
 *
 * @author limoukun
 * @since 2026/3/4
 **/
public interface DataSourceFactory {

    void setProperties(Properties properties);

    DataSource getDataSource();
}
