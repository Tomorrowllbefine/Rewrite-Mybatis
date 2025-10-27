package com.kk.mybatis.binding;

import com.kk.mybatis.binding.dao.ISchoolDao;
import com.kk.mybatis.binding.dao.IUserDao;
import com.kk.mybatis.session.SqlSession;
import com.kk.mybatis.session.SqlSessionFactory;
import com.kk.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiTest {

    private Logger logger = LoggerFactory.getLogger(ApiTest.class);

    @Test
    public void test_MapperProxyFactory() {
        // 注册
        MapperRegistry mapperRegistry = new MapperRegistry();
        mapperRegistry.addMappers("com.kk.mybatis.binding.dao");

        // 从SqlSession工厂中获取SqlSession
        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(mapperRegistry);
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
}
