package com.kk.mybatis.mapping;

import java.util.Map;

/**
 * 从配置文件中解析的参数：sql语句、入参类型、出参类型、动态参数及次序 等配置信息
 *
 * @author limoukun
 * @since 2026/3/8
 **/
public class BoundSql {

    /** sql **/
    private String sql;
    /** 参数类型 **/
    private String parameterType;
    /** 结果类型 **/
    private String resultType;
    /** 参数占位列表 **/
    private Map<Integer, String> parameter;

    public BoundSql(String sql, String parameterType, String resultType, Map<Integer, String> parameter) {
        this.sql = sql;
        this.parameterType = parameterType;
        this.resultType = resultType;
        this.parameter = parameter;
    }

    public String getSql() {
        return sql;
    }
    public String getParameterType() {
        return parameterType;
    }
    public String getResultType() {
        return resultType;
    }
    public Map<Integer, String> getParameter() {
        return parameter;
    }
}
