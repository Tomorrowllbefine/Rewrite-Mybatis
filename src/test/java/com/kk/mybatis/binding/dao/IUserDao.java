package com.kk.mybatis.binding.dao;

import com.kk.mybatis.binding.po.User;

import java.util.List;

public interface IUserDao {
    User queryById(Long id);
    String queryUserName(String uId);
    Integer queryUserAge(String uId);
}
