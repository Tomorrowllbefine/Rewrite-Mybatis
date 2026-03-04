package com.kk.mybatis.session.datasource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.kk.mybatis.session.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Druid 数据源工厂
 *
 * @author limoukun
 * @since 2026/3/4
 **/
public class DruidDataSourceFactory  implements DataSourceFactory {

    private Properties props;

    @Override
    public void setProperties(Properties properties) {
        this.props = properties;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(props.getProperty("driver"));
        dataSource.setUrl(props.getProperty("url"));
        dataSource.setUsername(props.getProperty("username"));
        dataSource.setPassword(props.getProperty("password"));
        return dataSource;

    }
}
