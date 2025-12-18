package com.kk.mybatis.session;

import com.kk.mybatis.builder.xml.XMLConfigBuilder;
import com.kk.mybatis.session.defaults.DefaultSqlSessionFactory;

import java.io.Reader;

/**
 * SqlSessionFactory 构建工厂
 * <p>
 * 工作流程：
 * 1. 用户提供一个XML配置文件（如mybatis-config.xml）
 * 2. 使用SqlSessionFactoryBuilder.build(Reader)方法加载并解析XML文件
 * 3. SqlSessionFactoryBuilder内部使用XMLConfigBuilder解析XML
 * 4. XMLConfigBuilder读取XML配置，特别是<mappers>部分，通过包扫描注册Mapper接口
 * 5. 最终生成完整的Configuration对象，并用于构建SqlSessionFactory
 *
 * @author limoukun
 * @since 2025/11/3
 **/
public class SqlSessionFactoryBuilder {

    /**
     * 根据XML配置文件构建SqlSessionFactory
     * 此方法负责：
     * 1. 创建XMLConfigBuilder来解析XML配置
     * 2. 调用parse方法解析XML内容
     * 3. 获取解析后的Configuration对象
     * 4. 使用Configuration创建DefaultSqlSessionFactory实例
     *
     * @param reader XML配置文件的Reader对象
     * @return SqlSessionFactory实例
     */
    public SqlSessionFactory build(Reader reader) {
        // 1. 创建XML配置构建器，用于解析传入的XML配置文件
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder(reader);
        
        // 2. 解析XML配置文件并获取Configuration对象
        Configuration configuration = xmlConfigBuilder.parse();
        
        // 3. 使用解析得到的Configuration构建SqlSessionFactory
        return build(configuration);
    }

    /**
     * 构建SqlSessionFactory的重载方法
     * 可用于其他构建方式，目前留空待实现
     *
     * @return SqlSessionFactory实例
     */
    public SqlSessionFactory build(Configuration configuration) {
        return new DefaultSqlSessionFactory(configuration);
    }
}