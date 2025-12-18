package com.kk.mybatis.io;

public class Resources {

    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
}
