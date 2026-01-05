package com.kk.mybatis.binding;

import com.kk.mybatis.binding.dao.ISchoolDao;
import com.kk.mybatis.binding.dao.IUserDao;
import com.kk.mybatis.mapping.MappedStatement;
import com.kk.mybatis.session.Configuration;
import com.kk.mybatis.session.SqlSession;
import com.kk.mybatis.session.SqlSessionFactory;
import com.kk.mybatis.session.SqlSessionFactoryBuilder;
import com.kk.mybatis.session.defaults.DefaultSqlSession;
import com.kk.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;

public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_MapperProxyFactory() {
        // 注册
        Configuration configuration = new Configuration();
        configuration.addMappers("com.kk.mybatis.binding.dao");

        // 从SqlSession工厂中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 获取Mapper
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        ISchoolDao schoolDao = sqlSession.getMapper(ISchoolDao.class);

        // 测试
        String userName = userDao.queryUserName("10001");
        logger.info("测试结果：{}", userName);

        String schoolList = schoolDao.querySchoolNamesStr();
        logger.info("测试结果：{}", schoolList);
    }

    /**
     * 测试使用XML配置的方式构建SqlSessionFactory
     * 工作流程：
     * 1. 加载XML配置文件mybatis-config.xml
     * 2. 使用SqlSessionFactoryBuilder.build(Reader)方法构建SqlSessionFactory
     * 3. 通过SqlSessionFactory获取SqlSession
     * 4. 从SqlSession中获取Mapper并进行测试
     */
    @Test
    public void test_XmlConfig() throws IOException {
        // 1. 加载XML配置文件
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml");
        Reader reader = new InputStreamReader(inputStream);

        // 2. 使用SqlSessionFactoryBuilder构建SqlSessionFactory
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);

        // 3. 获取SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        // 4. 获取Mapper
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
//        ISchoolDao schoolDao = sqlSession.getMapper(ISchoolDao.class);

        // 5. 测试
        String userName = userDao.queryUserName("10001");
        logger.info("测试结果：{}", userName);

//        String schoolList = schoolDao.querySchoolNamesStr();
//        logger.info("测试结果：{}", schoolList);
        
        // 6. 验证MappedStatement是否正确注册
        Configuration configuration = ((DefaultSqlSession) sqlSession).getConfiguration();
        Collection<MappedStatement> mappedStatements = configuration.getMappedStatements();
        logger.info("MappedStatements数量: {}", mappedStatements.size());
        for (MappedStatement mappedStatement : mappedStatements) {
            logger.info("MappedStatement ID: {}, SQL: {}, Type: {}",
                       mappedStatement.getId(),
                       mappedStatement.getSql(),
                       mappedStatement.getSqlCommandType());
        }
    }
}